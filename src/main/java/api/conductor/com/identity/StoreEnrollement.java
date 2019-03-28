package api.conductor.com.identity;

import java.io.Serializable;

import java.security.PrivateKey;

import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Service
public class StoreEnrollement implements Enrollment, Serializable {
	private static final long serialVersionUID = 7987536981920764357L;
	private PrivateKey privateKey;
	private String certificate;

	public StoreEnrollement(PrivateKey privateKey, String certificate) {
		this.certificate = certificate;
		this.privateKey = privateKey;
	}

	public PrivateKey getKey() {
		return privateKey;
	}

	public String getCert() {
		return certificate;
	}
}
