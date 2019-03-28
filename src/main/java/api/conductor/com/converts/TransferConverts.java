package api.conductor.com.converts;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.conductor.chaincodes.Transfer;
import api.conductor.com.dto.TransferDTO;

public class TransferConverts {
	private static Logger LOGGER = LoggerFactory.getLogger(AccountConverts.class);

	public static Transfer toEntity(TransferDTO transferModel) {
		ModelMapper modelMapper = new ModelMapper();
		LOGGER.info("Convert CardModel to Account");

		Transfer transfer = modelMapper.map(transferModel, Transfer.class);
		transfer.setArgs(new String[] {transfer.getAccountPayer(), transfer.getAccountReceiver(), transfer.getAmount()});
		
		LOGGER.info("Return {}", transfer.toString());

		return transfer;
	}
}
