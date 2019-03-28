package api.conductor.chaincodes;

import api.conductor.com.chaincodes.base.ChaincodeFeatures;
import api.conductor.com.paths.ChaincodesList;
import lombok.Data;

@Data
public class Transfer extends ChaincodeFeatures {
	private final static String nameCC = ChaincodesList.CC_TRANSFER;
	private String accountPayer;
	private String accountReceiver;
	private String amount;

	public Transfer() {
		super(nameCC);
	}

}
