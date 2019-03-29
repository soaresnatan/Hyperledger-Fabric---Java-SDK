package api.conductor.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import api.conductor.com.identity.Identity;
import api.conductor.com.identity.StoreEnrollement;

public class CreateIdentity {
	
	public static void main(String[] args) {
		try {
			String certificate;
			certificate = new String(IOUtils.toByteArray(new FileInputStream("./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem")), "UTF-8");
			PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream("./basic-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/cd96d5260ad4757551ed4a5a991e62130f8008a0bf996e4e4b84cd097a747fec_sk")));
			
			//certificate = new String(IOUtils.toByteArray(new FileInputStream("./network/crypto-config/peerOrganizations/org3-be-com/users/Admin@org3-be-com/msp/signcerts/Admin@org3-be-com-cert.pem")), "UTF-8");
			//PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream("./network/crypto-config/peerOrganizations/org3-be-com/users/Admin@org3-be-com/msp/keystore/0c53ba1c5993219f2bee87e2c545ddccf934095962d11955f3040270ec873707_sk")));
			StoreEnrollement storeEnroll = new StoreEnrollement(privateKey, certificate);
			System.out.print(storeEnroll.getCert());
			Identity identityTmp = new Identity("adminorg1", "Org1", "Org1MSP", storeEnroll);
			serializeObject("./wallet/adminorg1.dat",identityTmp);
		} catch (IOException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PrivateKey getPrivateKeyFromBytes(byte[] data)
			throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
		final Reader pemReader = new StringReader(new String(data));

		final PrivateKeyInfo pemPair;
		try (PEMParser pemParser = new PEMParser(pemReader)) {
			pemPair = (PrivateKeyInfo) pemParser.readObject();
		}

		PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)
				.getPrivateKey(pemPair);
		return privateKey;
	}
	
	public static void serializeObject(String path, Identity identity) throws IOException {
		System.out.println("------> path " + path);
		File f = new File(path);
		if(!f.exists())
				f.createNewFile();
		
		FileOutputStream arquivoGrav = new FileOutputStream(f);
		ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);

		objGravar.writeObject(identity);
		objGravar.flush();
		objGravar.close();
		arquivoGrav.flush();
		arquivoGrav.close();

		System.out.println("Objeto gravado com sucesso!");
	}
		
}
