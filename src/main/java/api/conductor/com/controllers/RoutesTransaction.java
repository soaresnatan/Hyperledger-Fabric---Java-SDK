package api.conductor.com.controllers;

import api.conductor.com.connections.ChannelFabric;
import api.conductor.com.connections.ConnectionFabric;
import api.conductor.com.connections.NetworkFabric;
import api.conductor.com.connections.SessionConnection;
import api.conductor.com.services.BlocksServices;
import api.conductor.com.services.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/transaction")
@Api(value = "Título", description = "Descrição")
public class RoutesTransaction {
	@Autowired
	private BlocksServices blockServices;

	@ApiOperation(value = "Descrição")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso"), })
	@GetMapping(path = "/id/{tx}")
	public ResponseEntity<String> queryTransaction(@PathVariable("tx") String tx, HttpServletRequest request)
			throws NetworkConfigurationException, IOException {
		try {
			SessionConnection sessionConnection = (SessionConnection) request.getSession()
					.getAttribute("SESSION_CONNECTION");

			if (sessionConnection == null) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(
					blockServices.queryTransaction(sessionConnection.getChannelFabric().getChannel(), tx),
					HttpStatus.OK);
		} catch (InvalidArgumentException | ProposalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>((MultiValueMap<String, String>) e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
