package api.conductor.com.controllers;

import api.conductor.chaincodes.Card;
import api.conductor.com.connections.ChannelFabric;
import api.conductor.com.connections.ConnectionFabric;
import api.conductor.com.connections.SessionConnection;
import api.conductor.com.converts.CardConverts;
import api.conductor.com.dto.CardDTO;
import api.conductor.com.responses.Response;
import api.conductor.com.responses.ResponsePeer;
import api.conductor.com.responses.ResponseQuery;
import api.conductor.com.services.ChaincodeServices;
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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/chaincode/card")
@Api(value = "Título", description = "Descrição")
public class RoutesCard {
	
	@Autowired
    private ChaincodeServices chaincodeServices;

    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity<List<Response>> invoketeste(@RequestBody CardDTO cardModel, HttpServletRequest request) {
        try {
        	SessionConnection sessionConnection = (SessionConnection) request.getSession()
    				.getAttribute("SESSION_CONNECTION");
        	
        	if (sessionConnection == null) {
    			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    		}
            Card card = CardConverts.toEntity(cardModel);
            card.setMethod("Create");
            return new  ResponseEntity<>(chaincodeServices.invoke(sessionConnection.getConnectionFabric().getConnection(), sessionConnection.getChannelFabric().getChannel(), card), HttpStatus.OK);
        } catch (InvalidArgumentException | ProposalException | InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>((List<Response>) e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/getbynumber/{cardNumber}")
    public ResponseEntity<List<Response>> queryteste(CardDTO cardModel, HttpServletRequest request)
            throws com.google.protobuf.TextFormat.ParseException {
        try {        	
        	SessionConnection sessionConnection = (SessionConnection) request.getSession()
    				.getAttribute("SESSION_CONNECTION");
        	
        	if (sessionConnection == null) {
    			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    		}
        	
        	
            Card card = CardConverts.toEntity(cardModel);
            card.setMethod("GetByNumber");
            return new ResponseEntity<>(chaincodeServices.query(sessionConnection.getConnectionFabric().getConnection(), sessionConnection.getChannelFabric().getChannel(), card), HttpStatus.OK);
        } catch (InvalidArgumentException | ProposalException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>((List<Response>) e, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
