package api.conductor.com.converts;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.conductor.chaincodes.Account;
import api.conductor.com.dto.AccountDTO;

public class AccountConverts {
	private static Logger LOGGER = LoggerFactory.getLogger(AccountConverts.class);

	public static Account toEntity(AccountDTO accountModel) {
		ModelMapper modelMapper = new ModelMapper();
		LOGGER.info("Convert AccountModel to Account");

		Account account = modelMapper.map(accountModel, Account.class);
		account.setArgs(new String[] {account.getNumber(), account.getBalance(), account.getOwner()});
		
		LOGGER.info("Return {}", account.toString());

		return account;
	}

}
