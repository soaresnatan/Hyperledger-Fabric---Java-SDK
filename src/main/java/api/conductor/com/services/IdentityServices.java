package api.conductor.com.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.conductor.com.dto.IdentityDTO;
import api.conductor.com.identity.Identity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Service
public class IdentityServices {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private Identity identity;
	private String walletPATH = "./wallet/";

	public List<IdentityDTO> listIdentities() {
		List<IdentityDTO> identitiesReturn = new ArrayList<>();
		
		File wallet = new File(walletPATH);
		File[] identities = wallet.listFiles();
		LOGGER.info("List identities from {}",walletPATH);
		
		for (int it = 0; it < identities.length; it++) {

			identitiesReturn.add(new IdentityDTO(identities[it].getName()));

			LOGGER.info("Identity {}", identities[it].getName());
		}

		return identitiesReturn;
	}

	public Identity checkIdentity(String identityName) throws ClassNotFoundException, IOException {
		identity = null;
		LOGGER.info("Check identity {}", identityName);

		String credentialPath = walletPATH + identityName + ".dat";
		LOGGER.info("Check credential path {}", credentialPath);
		
		if (Files.exists(Paths.get(credentialPath))) {			
			identity = deserializeObject(credentialPath);
			LOGGER.info("Identity exists");
		}
		
		LOGGER.info("Return {}", identity.getName());
		return identity;
	}

	public String registerIdentity(HFCAClient caClient, String newUser, String organizationName) {
		String response = "Error";
		LOGGER.info("New identity {}", newUser);
		LOGGER.info("Organization {}", organizationName);
		try {			
			String filePath = walletPATH + "adminca" + organizationName + ".dat";
			Identity adminca = deserializeObject(filePath);			
			LOGGER.info("Try get ADMIN CA In file {}",filePath);
						
			if (adminca == null) {
				LOGGER.info("Admin not found");
				
				Enrollment adminEnrollment = caClient.enroll("admin", "adminpw");
				adminca = new Identity("adminca" + organizationName, organizationName, organizationName + "MSP",
						adminEnrollment);
				LOGGER.info("Create {}", adminca.getName());
				
				LOGGER.info("Try serialize");
				serializeObject(filePath, adminca);
			}

			LOGGER.info("Using {} to register {}", adminca.getName(), newUser);
			RegistrationRequest registrationRequest = new RegistrationRequest(newUser, organizationName);
			String enrollmentSecret = caClient.register(registrationRequest, adminca);
			Enrollment enrollment = caClient.enroll(newUser, enrollmentSecret);
			
			Identity identityTmp = new Identity(newUser, organizationName, organizationName + "MSP", enrollment);
			LOGGER.info("Registered {}", identityTmp.getName());

			LOGGER.info("Try serialize");
			serializeObject(walletPATH + newUser + ".dat", identityTmp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "   response :" + response;
	}

	public Identity deserializeObject(String filePath) throws ClassNotFoundException, IOException {	
		File file = new File(filePath);
		if(!file.exists()) {
			LOGGER.info("Faile to open {}",filePath);
			return null;
		}
		
		FileInputStream arquivoLeitura = new FileInputStream(file);
		ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);
		LOGGER.info("Open file {}", file);

		Identity identity = (Identity) objLeitura.readObject();
		LOGGER.info("Get {}", identity.getName());
		
		objLeitura.close();
		arquivoLeitura.close();
		LOGGER.info("Close file");
		
		return identity;
	}

	public void serializeObject(String path, Identity identity) throws IOException {
		LOGGER.info("Serialize in {}",path);
		File f = new File(path);
		if (!f.exists()) {
			f.createNewFile();
			LOGGER.info("Create file");
		}
		
		LOGGER.info("Open file");
		FileOutputStream arquivoGrav = new FileOutputStream(f);
		ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
		
		LOGGER.info("Write file");
		objGravar.writeObject(identity);
		objGravar.flush();
		objGravar.close();
		
		LOGGER.info("Close file");
		arquivoGrav.flush();
		arquivoGrav.close();

		LOGGER.info("Finish serialize");
	}

}
