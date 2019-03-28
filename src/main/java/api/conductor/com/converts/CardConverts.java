package api.conductor.com.converts;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.conductor.chaincodes.Card;
import api.conductor.com.dto.CardDTO;

public class CardConverts {
	private static Logger LOGGER = LoggerFactory.getLogger(AccountConverts.class);

	public static Card toEntity(CardDTO accountModel) {
		ModelMapper modelMapper = new ModelMapper();
		LOGGER.info("Convert CardModel to Account");

		Card card = modelMapper.map(accountModel, Card.class);
		card.setArgs(new String[] {card.getCardNumber(), card.getAccountNumber()});
		
		LOGGER.info("Return {}", card.toString());

		return card;
	}
}
