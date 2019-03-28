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
			certificate = new String(IOUtils.toByteArray(new FileInputStream("./network-test/crypto-config/peerOrganizations/org2-domain-com/users/Admin@org2-domain-com/msp/signcerts/Admin@org2-domain-com-cert.pem")), "UTF-8");
			PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream("./network-test/crypto-config/peerOrganizations/org2-domain-com/users/Admin@org2-domain-com/msp/keystore/8146a1a433a560271aeb68bbbf365c0b478f229938d4b2492cee349ba27ef0e6_sk")));
			
			//certificate = new String(IOUtils.toByteArray(new FileInputStream("./network/crypto-config/peerOrganizations/org3-be-com/users/Admin@org3-be-com/msp/signcerts/Admin@org3-be-com-cert.pem")), "UTF-8");
			//PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream("./network/crypto-config/peerOrganizations/org3-be-com/users/Admin@org3-be-com/msp/keystore/0c53ba1c5993219f2bee87e2c545ddccf934095962d11955f3040270ec873707_sk")));
			StoreEnrollement storeEnroll = new StoreEnrollement(privateKey, certificate);
			System.out.print(storeEnroll.getCert());
			Identity identityTmp = new Identity("org2", "org2", "org2MSP", storeEnroll);
			serializeObject("./wallet/orgatest2.dat",identityTmp);
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
