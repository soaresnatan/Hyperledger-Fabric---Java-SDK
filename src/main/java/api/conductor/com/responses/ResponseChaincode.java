package api.conductor.com.responses;

import java.util.ArrayList;
import java.util.List;

import api.conductor.com.chaincodes.base.Chaincode;
import lombok.Data;

@Data
public class ResponseChaincode extends Response {
	private String peer;
	private List<Chaincode> chaincode;
	
	public ResponseChaincode(String peer) {
		this.peer = peer;
		chaincode = new ArrayList<>();		
	}
	
	public void addChaincode(String name, String version) {
		chaincode.add(new Chaincode(name, version));
	}
	
}
		