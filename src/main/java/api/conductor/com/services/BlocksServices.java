package api.conductor.com.services;

import java.io.IOException;
import java.util.Collection;

import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlocksServices {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public String queryTransaction(Channel channel, String tx)
			throws ProposalException, InvalidArgumentException, NetworkConfigurationException, IOException {

		LOGGER.info("Get tx {}", tx);
		LOGGER.info("From {}", channel.getName());
		LOGGER.info("With {}", channel.getPeers().iterator().next());
		TransactionInfo response = channel.queryTransactionByID(channel.getPeers().iterator().next(), tx);

		return response.getEnvelope().getPayload().toStringUtf8();
	}
	
	public String queryBlockByNumber(Channel channel, long numberBlock)
			throws ProposalException, InvalidArgumentException, NetworkConfigurationException, IOException {

		LOGGER.info("Get block {}", numberBlock);
		LOGGER.info("From {}", channel.getName());
		LOGGER.info("With {}", channel.getPeers().iterator().next());
		BlockInfo respose = channel.queryBlockByNumber(numberBlock);
		
		return null;
	}

	
}
