package api.conductor.com.connections;

import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PreDestroy;

import org.hyperledger.fabric.protos.common.Common.Block;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockListener;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
@Scope("prototype")
public class ChannelFabric {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private Channel channel;
	
	public Channel getChannel() {
		return channel;
	}

	@PreDestroy
	public void clear() {
		LOGGER.info("Clear Channel Fabric");
		try {
			LOGGER.info("Channel initialized {}", channel.isInitialized());
			channel.shutdown(true);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("Empty channel");
		}
	}

	public void setChannel(HFClient client, NetworkConfig network, String channelName)
			throws InvalidArgumentException, TransactionException, NetworkConfigurationException, IOException {

		try {
			LOGGER.info("Channel initialized {}", channel.isInitialized());
			if (channel.isInitialized())
				channel.shutdown(true);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("Empty channel");
		}

		LOGGER.info("Try from file");
		channel = client.loadChannelFromConfig(channelName, network);
		channel.initialize();

		LOGGER.info("Channel connection {}", channel.getName());
		LOGGER.info("Client context {}", client.getUserContext().getName());
	}


}
