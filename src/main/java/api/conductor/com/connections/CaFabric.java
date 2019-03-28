package api.conductor.com.connections;

import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.NetworkConfig.CAInfo;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import api.conductor.com.dto.CaDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Service
@Scope("prototype")
public class CaFabric {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private HFCAClient caClient;
	private String organizationName;

	public CaDTO initializesCommunication(NetworkConfig networkConfig, String organization) throws Exception {
		LOGGER.info("Initializes HFCAClient");
		
		NetworkConfig.OrgInfo organizationInfo = networkConfig.getOrganizationInfo(organization);
		
		
		CAInfo caInfo = organizationInfo.getCertificateAuthorities().get(0);		
		caClient = HFCAClient.createNewInstance(caInfo);
		LOGGER.info("Connect to {}", caClient.getCAName());
						
		HFCAInfo info = caClient.info();
		organizationName = organization;
		LOGGER.info("Organization {}", organizationName);
		
		return new CaDTO(info.getCAName());
	}
}
