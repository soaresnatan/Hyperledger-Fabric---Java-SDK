package api.conductor.com.controllers;

import api.conductor.com.connections.CaFabric;
import api.conductor.com.connections.ConnectionFabric;
import api.conductor.com.connections.NetworkFabric;
import api.conductor.com.connections.SessionConnection;
import api.conductor.com.dto.CaDTO;
import api.conductor.com.dto.IdentityDTO;
import api.conductor.com.identity.Identity;
import api.conductor.com.services.IdentityServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/wallet")
@Api(value = "Título", description = "Descrição")
public class RoutesWallet {
	
	@Autowired 
	private AutowireCapableBeanFactory beanFactory;
	
	@Autowired
    private CaFabric caFabric;

	@Autowired
    private IdentityServices identityServices;

	@ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @GetMapping(path = "/identities")
    public ResponseEntity<List<IdentityDTO>> listidentities(HttpServletRequest request) {
		SessionConnection sessionConnection = (SessionConnection) request.getSession()
				.getAttribute("SESSION_CONNECTION");
    	
    	if (sessionConnection == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<>(identityServices.listIdentities(), HttpStatus.OK);
    }


    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @PostMapping(path = "/ca")
    public ResponseEntity<CaDTO> initca(@RequestBody CaDTO caModel, HttpServletRequest request) throws Exception {
    	SessionConnection sessionConnection = (SessionConnection) request.getSession()
				.getAttribute("SESSION_CONNECTION");
    	
    	if (sessionConnection == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
        return new ResponseEntity<>(caFabric.initializesCommunication(sessionConnection.getNetworkFabric().getNetworkConfig(), caModel.getOrganization()), HttpStatus.OK);
    }


    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @PostMapping(path = "/register" )
    public ResponseEntity<String> registerIdentity(@RequestBody IdentityDTO user, HttpServletRequest request) {
    	SessionConnection sessionConnection = (SessionConnection) request.getSession()
				.getAttribute("SESSION_CONNECTION");
    	
    	if (sessionConnection == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<>(identityServices.registerIdentity(caFabric.getCaClient(), user.getIdentity(),
                caFabric.getOrganizationName()), HttpStatus.OK);
    }


    @ApiOperation(value = "Descrição")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso"),
            }
    )
    @PostMapping(path = "/identity")
    public ResponseEntity<String> checkIdentity(@RequestBody IdentityDTO user, HttpServletRequest request) throws InvalidArgumentException, NetworkConfigurationException, IOException, ClassNotFoundException {
        try {
        	SessionConnection sessionConnection = (SessionConnection) request.getSession()
    				.getAttribute("SESSION_CONNECTION");

    		if (sessionConnection == null) {
    			sessionConnection = new SessionConnection();
    			beanFactory.autowireBean(sessionConnection);
    		}

    		identityServices.checkIdentity(user.getIdentity());
    		Identity identity = identityServices.getIdentity();

    		sessionConnection.getConnectionFabric().initializesConnection();
    		sessionConnection.getConnectionFabric().setContext(identity);
    		
    		try {
    			sessionConnection.getNetworkFabric().initializate(sessionConnection.getConnectionFabric().getConnection(),
    					identity.getAffiliation());
    		} catch (InvalidArgumentException | NetworkConfigurationException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		request.getSession().setAttribute("SESSION_CONNECTION", sessionConnection);
    		
            return new ResponseEntity<>(sessionConnection.getConnectionFabric().getConnection().getUserContext().getName(), HttpStatus.OK);
        }
        catch (IOException e){
            return new ResponseEntity<>((MultiValueMap<String, String>) e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
