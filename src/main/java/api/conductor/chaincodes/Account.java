package api.conductor.chaincodes;

import api.conductor.com.chaincodes.base.ChaincodeFeatures;
import api.conductor.com.paths.ChaincodesList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class Account extends ChaincodeFeatures {
	private final static String nameCC = ChaincodesList.CC_ACCOUNT;
	private String number;
	private String balance;
	private String owner;
	
	public Account() {
		super(nameCC);
	}
			
}
