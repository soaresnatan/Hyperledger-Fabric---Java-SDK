package api.conductor.com.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hyperledger.fabric.protos.peer.Query.ChaincodeInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.conductor.com.responses.Response;
import api.conductor.com.responses.ResponseChaincode;
import api.conductor.com.responses.ResponseChannel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class ChannelService {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public List<Response> queryInstalledChaincodes(HFClient client, Channel channel, Collection<Peer> peersOrg)
			throws ProposalException, InvalidArgumentException, NetworkConfigurationException, IOException {
		List<Response> responseChannel = new ArrayList<>();
		LOGGER.info("Query installed chaincodes");
		
		for (Peer peerInfo : peersOrg) {
			ResponseChaincode txChannel = new ResponseChaincode(peerInfo.getName());
			LOGGER.info("Peer: {}",peerInfo.getName());
			for (ChaincodeInfo chaincodeInfo : client.queryInstalledChaincodes(peerInfo)) {
				LOGGER.info("Name: {}",chaincodeInfo.getName());
				LOGGER.info("Version {}", chaincodeInfo.getVersion());
				txChannel.addChaincode(chaincodeInfo.getName(), chaincodeInfo.getVersion());
			}			
			responseChannel.add(txChannel);
		}
		return responseChannel;
	}

	public Collection<Response> queryInstantiatedChaincodes(Channel channel)
			throws InvalidArgumentException, ProposalException, NetworkConfigurationException, IOException {
		List<Response> responseChannel = new ArrayList<>();
		Collection<Peer> queryPeers = channel.getPeers();
		
		LOGGER.info("Query instantiated chaincodes in channel");
		for (Peer peerInfo : queryPeers) {
			ResponseChaincode txChannel = new ResponseChaincode(peerInfo.getName());
			LOGGER.info("Peer: {}",peerInfo.getName());
			for (ChaincodeInfo chaincodeInfo : channel.queryInstantiatedChaincodes(peerInfo)) {
				LOGGER.info("Name: {}",chaincodeInfo.getName());
				LOGGER.info("Version {}", chaincodeInfo.getVersion());
				txChannel.addChaincode(chaincodeInfo.getName(), chaincodeInfo.getVersion());
			}
			responseChannel.add(txChannel);
		}

		return responseChannel;
	}

	public List<Response> channelList(HFClient client, Collection<Peer> peersOrg) throws ProposalException, InvalidArgumentException, TransactionException,
			NetworkConfigurationException, IOException {
		List<Response> responseChannel = new ArrayList<>();
		
		LOGGER.info("Query the channels that the organization belongs to");		
		for (Peer peerInfo : peersOrg) {
			ResponseChannel txChannel = new ResponseChannel(peerInfo.getName());

			LOGGER.info("Peer: {}",peerInfo.getName());
			for (String resultChannel : client.queryChannels(peerInfo)) {
				LOGGER.info("Name: {}",resultChannel);
				txChannel.addChannel(resultChannel);
			}
			responseChannel.add(txChannel);
		}

		return responseChannel;
	}
}
