package api.conductor.com.controllers;

import api.conductor.com.connections.ChannelFabric;
import api.conductor.com.connections.ConnectionFabric;
import api.conductor.com.connections.NetworkFabric;
import api.conductor.com.connections.SessionConnection;
import api.conductor.com.dto.ChannelDTO;
import api.conductor.com.responses.Response;
import api.conductor.com.responses.ResponseChaincode;
import api.conductor.com.responses.ResponseChannel;
import api.conductor.com.services.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/channel")
@Api(value = "Título", description = "Descrição")
public class RoutesChannel {

	@Autowired
	private ChannelService channelServices;

	@ApiOperation(value = "Descrição")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"), })
	@PostMapping(path = "/connect")
	public ResponseEntity<?> connectionchannel(@RequestBody ChannelDTO channelName, HttpServletRequest request)
			throws InvalidArgumentException, TransactionException, NetworkConfigurationException, IOException {
		try {
			SessionConnection sessionConnection = (SessionConnection) request.getSession()
					.getAttribute("SESSION_CONNECTION");

			if (sessionConnection == null) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			sessionConnection.getChannelFabric().setChannel(sessionConnection.getConnectionFabric().getConnection(),
					sessionConnection.getNetworkFabric().getNetworkConfig(), channelName.getChannel());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (InvalidArgumentException | IOException e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Descrição")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"), })
	@GetMapping(path = "/list")
	public ResponseEntity<List<Response>> channelsList(HttpServletRequest request) throws ProposalException,
			InvalidArgumentException, TransactionException, NetworkConfigurationException, IOException {
		try {
			SessionConnection sessionConnection = (SessionConnection) request.getSession()
					.getAttribute("SESSION_CONNECTION");

			if (sessionConnection == null) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(
					channelServices.channelList(sessionConnection.getConnectionFabric().getConnection(),
							sessionConnection.getNetworkFabric().getPeersOrg()),
					HttpStatus.OK);
		} catch (InvalidArgumentException | IOException e) {
			return new ResponseEntity<>((MultiValueMap<String, String>) e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
