package api.conductor.com.converts;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.conductor.com.chaincodes.base.ChaincodeMaintenance;
import api.conductor.com.dto.ChaincodeDTO;


public class ChaincodeMaintenanceConverts {
	private static Logger LOGGER = LoggerFactory.getLogger(AccountConverts.class);

	public static ChaincodeMaintenance toEntity(ChaincodeDTO chaincodeModel) {
		ModelMapper modelMapper = new ModelMapper();
		LOGGER.info("Convert chaincodeDTO to chaincodeMaintenance");

		ChaincodeMaintenance chaincodeMaintenance = modelMapper.map(chaincodeModel, ChaincodeMaintenance.class);

		LOGGER.info("Return {}", chaincodeMaintenance.toString());
		return chaincodeMaintenance;
	}
}
