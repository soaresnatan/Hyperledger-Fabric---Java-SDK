package api.conductor.com.controllers;

import api.conductor.chaincodes.Transfer;
import api.conductor.com.connections.CaFabric;
import api.conductor.com.connections.ChannelFabric;
import api.conductor.com.connections.ConnectionFabric;
import api.conductor.com.connections.NetworkFabric;
import api.conductor.com.connections.SessionConnection;
import api.conductor.com.converts.TransferConverts;
import api.conductor.com.dto.TransferDTO;
import api.conductor.com.responses.Response;
import api.conductor.com.responses.ResponsePeer;
import api.conductor.com.services.ChaincodeServices;
import api.conductor.com.services.ChannelService;
import api.conductor.com.services.IdentityServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/chaincode/transfer")
@Api(value = "Título", description = "Descrição")
public class TransferController {

    @Autowired
    private ChaincodeServices chaincodeServices;

    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @RequestMapping(method = RequestMethod.POST, value = "/money")
    public ResponseEntity<List<Response>> invoketeste(@RequestBody TransferDTO transferModel, HttpServletRequest request) {
        try {
        	SessionConnection sessionConnection = (SessionConnection) request.getSession()
					.getAttribute("SESSION_CONNECTION");

			if (sessionConnection == null) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
            Transfer transfer = TransferConverts.toEntity(transferModel);
            transfer.setMethod("Money");
            return new ResponseEntity<>(chaincodeServices.invoke(sessionConnection.getConnectionFabric().getConnection(), sessionConnection.getChannelFabric().getChannel(), transfer), HttpStatus.OK);
        } catch (InvalidArgumentException | ProposalException | InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>((List<Response>)e, HttpStatus.OK);
        }
    }
}
