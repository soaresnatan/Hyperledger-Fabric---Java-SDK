package api.conductor.com.chaincodes.base;

import org.hyperledger.fabric.sdk.ChaincodeID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChaincodeMaintenance {
	private Chaincode chaincode;
	private String path;
	private String endorsement;
	
	public String getVersion() {
		return chaincode.getVersion();
	}
	
	public ChaincodeID getChaincodeID() {
		return ChaincodeID.newBuilder().setName(chaincode.getName()).setVersion(chaincode.getVersion()).setPath(path).build();
	}
	
}
