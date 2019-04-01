package api.conductor.com.controllers;

import api.conductor.com.chaincodes.base.ChaincodeMaintenance;
import api.conductor.com.connections.ChannelFabric;
import api.conductor.com.connections.ConnectionFabric;
import api.conductor.com.connections.NetworkFabric;
import api.conductor.com.connections.SessionConnection;
import api.conductor.com.converts.ChaincodeMaintenanceConverts;
import api.conductor.com.dto.ChaincodeDTO;
import api.conductor.com.responses.Response;
import api.conductor.com.responses.ResponsePeer;
import api.conductor.com.services.ChaincodeServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/chaincode")
@Api(value = "Título", description = "Descrição")
public class ChaincodesController {
	
	@Autowired
    private ChaincodeServices chaincodeServices;

    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @PostMapping(path = "/install")
    public ResponseEntity<List<Response>> installchaincode(@RequestBody ChaincodeDTO chaincode, HttpServletRequest request) throws NetworkConfigurationException, IOException {
        try {
        	SessionConnection sessionConnection = (SessionConnection) request.getSession()
    				.getAttribute("SESSION_CONNECTION");
        	
        	if (sessionConnection == null) {
    			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    		}
        	
            ChaincodeMaintenance chaincodeMaintenance = ChaincodeMaintenanceConverts.toEntity(chaincode);
            return new ResponseEntity<>(chaincodeServices.installChaincode(sessionConnection.getConnectionFabric().getConnection(), sessionConnection.getChannelFabric().getChannel(),
            		sessionConnection.getNetworkFabric().getPeersOrg(), chaincodeMaintenance), HttpStatus.OK);
        } catch (InvalidArgumentException | ProposalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>((List<Response>) e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @PostMapping(path = "/instantiate")
    public ResponseEntity<List<Response>> instantiatechaincode(@RequestBody ChaincodeDTO chaincode, HttpServletRequest request) {
        try {
        	SessionConnection sessionConnection = (SessionConnection) request.getSession()
    				.getAttribute("SESSION_CONNECTION");
        	
        	if (sessionConnection == null) {
    			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    		}
        	
            ChaincodeMaintenance chaincodeMaintenance = ChaincodeMaintenanceConverts.toEntity(chaincode);
            return new ResponseEntity<>(chaincodeServices.instantiateChaincode(sessionConnection.getConnectionFabric().getConnection(), sessionConnection.getChannelFabric().getChannel(),	chaincodeMaintenance), HttpStatus.OK);
        } catch (InvalidArgumentException | ProposalException | ChaincodeEndorsementPolicyParseException
                | IOException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<>((List<Response>) e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @PostMapping(path ="/upgrade")
    public ResponseEntity<List<Response>> upgradechaincode(@RequestBody ChaincodeDTO chaincode, HttpServletRequest request) {
        try {
        	SessionConnection sessionConnection = (SessionConnection) request.getSession()
    				.getAttribute("SESSION_CONNECTION");
        	
        	if (sessionConnection == null) {
    			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    		}
        	
            ChaincodeMaintenance chaincodeMaintenance = ChaincodeMaintenanceConverts.toEntity(chaincode);
            return new ResponseEntity<>(chaincodeServices.upgradeChaincode(sessionConnection.getConnectionFabric().getConnection(), sessionConnection.getChannelFabric().getChannel(), chaincodeMaintenance), HttpStatus.OK);
        } catch (InvalidArgumentException | ProposalException | ChaincodeEndorsementPolicyParseException
                | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>((List<Response>) e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
