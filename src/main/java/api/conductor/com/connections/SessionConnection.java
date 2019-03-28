package api.conductor.com.connections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import api.conductor.com.identity.Identity;
import api.conductor.com.services.ChaincodeServices;
import api.conductor.com.services.ChannelService;
import api.conductor.com.services.IdentityServices;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Component
@Scope("prototype")
public class SessionConnection {		
    @Autowired
    private NetworkFabric networkFabric;
	
	@Autowired
	private ConnectionFabric connectionFabric;

	@Autowired
	private ChannelFabric channelFabric;	
	
	@Autowired
	private CaFabric caFabric;
}
