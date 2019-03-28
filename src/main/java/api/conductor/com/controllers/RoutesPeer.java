package api.conductor.com.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.conductor.com.connections.SessionConnection;
import api.conductor.com.responses.Response;
import api.conductor.com.responses.ResponseChaincode;
import api.conductor.com.services.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/peer")
@Api(value = "Título", description = "Descrição")
public class RoutesPeer {
	@Autowired
	private ChannelService channelServices;
	
	@ApiOperation(value = "Descrição")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"), })
	@GetMapping(path = "/installedchaincodes")
	public ResponseEntity<List<Response>> queryinstalled(HttpServletRequest request)
			throws ProposalException, InvalidArgumentException, NetworkConfigurationException, IOException {
		SessionConnection sessionConnection = (SessionConnection) request.getSession()
				.getAttribute("SESSION_CONNECTION");

		if (sessionConnection == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(
				channelServices.queryInstalledChaincodes(sessionConnection.getConnectionFabric().getConnection(),
						sessionConnection.getChannelFabric().getChannel(),
						sessionConnection.getNetworkFabric().getPeersOrg()),
				HttpStatus.OK);
	}

	@ApiOperation(value = "Descrição")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"), })
	@GetMapping(path = "/instantiatedchaincodes")
	public ResponseEntity<Collection<Response>> queryinstantiatedchaincodes(HttpServletRequest request)
			throws NetworkConfigurationException, IOException {
		try {
			SessionConnection sessionConnection = (SessionConnection) request.getSession()
					.getAttribute("SESSION_CONNECTION");

			if (sessionConnection == null) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(
					channelServices.queryInstantiatedChaincodes(sessionConnection.getChannelFabric().getChannel()),
					HttpStatus.OK);
		} catch (InvalidArgumentException | ProposalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>((MultiValueMap<String, String>) e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
