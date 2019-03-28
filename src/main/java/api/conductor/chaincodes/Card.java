package api.conductor.chaincodes;

import api.conductor.com.chaincodes.base.ChaincodeFeatures;
import api.conductor.com.dto.AccountDTO;
import api.conductor.com.paths.ChaincodesList;
import lombok.Data;

@Data
public class Card extends ChaincodeFeatures {
	private final static String nameCC =  ChaincodesList.CC_CARD;
	private String cardNumber;
	private String accountNumber;
	
	public Card() {
		super(nameCC);
	}
	
}
