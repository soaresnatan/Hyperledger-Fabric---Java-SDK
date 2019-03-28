package api.conductor.com.services;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.hyperledger.fabric.protos.peer.FabricTransaction.Transaction;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChaincodeEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.UpgradeProposalRequest;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.TextFormat.ParseException;

import api.conductor.com.chaincodes.base.ChaincodeFeatures;
import api.conductor.com.chaincodes.base.ChaincodeMaintenance;
import api.conductor.com.responses.Response;
import api.conductor.com.responses.ResponsePeer;
import api.conductor.com.responses.ResponseQuery;
import api.conductor.com.responses.ResponseSdk;
import io.netty.util.concurrent.CompleteFuture;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class ChaincodeServices {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private ObjectMapper mapper = new ObjectMapper();
	private Collection<ProposalResponse> proposalResponses;
	private Collection<ProposalResponse> proposalSuccessful = new LinkedList<>();
	private Collection<ProposalResponse> proposalFailed = new LinkedList<>();

	public List<Response> installChaincode(HFClient client, Channel channel, Collection<Peer> peersOrg,
			ChaincodeMaintenance chaincode)
			throws InvalidArgumentException, ProposalException, NetworkConfigurationException, IOException {

		List<Response> responseInstallChaincode = new ArrayList<>();

		LOGGER.info("Start install chaincode");
		LOGGER.info("Create InstallProposalRequest");
		LOGGER.info("Object {}", chaincode.toString());

		InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
		installProposalRequest.setChaincodeID(chaincode.getChaincodeID());
		installProposalRequest.setChaincodeSourceLocation(new File("chaincode/"));
		installProposalRequest.setChaincodeVersion(chaincode.getVersion());
		installProposalRequest.setChaincodeLanguage(Type.GO_LANG);

		LOGGER.info("Infos");
		LOGGER.info("Name: {}", installProposalRequest.getChaincodeName());
		LOGGER.info("Version: {}", installProposalRequest.getChaincodeID().getVersion());
		LOGGER.info("Path: {}", installProposalRequest.getChaincodePath());
		LOGGER.info("Language: {}", installProposalRequest.getChaincodeLanguage());

		for (Peer peer : peersOrg)
			LOGGER.info("Send Request to {}", peer.getName());

		proposalSuccessful.clear();
		proposalFailed.clear();
		
		proposalResponses = client.sendInstallProposal(installProposalRequest, peersOrg);
		LOGGER.info("Get response");
		for (ProposalResponse response : proposalResponses) {
			LOGGER.info("Peer {}", response.getPeer().getName());
			LOGGER.info("Status {}", response.getStatus());
			LOGGER.info("Message {}", response.getMessage());
			LOGGER.info("Transaction Id {}", response.getTransactionID());

			responseInstallChaincode.add(new ResponsePeer(response.getPeer().getName(), response.getStatus().toString(),
					response.getMessage(), response.getTransactionID()));
			if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
				proposalSuccessful.add(response);
			} else {
				proposalFailed.add(response);
			}
		}
		LOGGER.info("Erros: {}", proposalFailed.size());

		return responseInstallChaincode;
	}

	public List<Response> instantiateChaincode(HFClient client, Channel channel, ChaincodeMaintenance chaincode)
			throws InvalidArgumentException, ProposalException, ChaincodeEndorsementPolicyParseException, IOException {
		List<Response> responseInstantiateChaincode = new ArrayList<>();

		LOGGER.info("Start Instantiate chaincode");
		LOGGER.info("Create InstantiateProposalRequest");
		LOGGER.info("Object {}", chaincode.toString());

		InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
		instantiateProposalRequest.setProposalWaitTime(120000);
		instantiateProposalRequest.setChaincodeID(chaincode.getChaincodeID());
		instantiateProposalRequest.setChaincodeLanguage(Type.GO_LANG);
		instantiateProposalRequest.setFcn("init");
		instantiateProposalRequest.setArgs(new String[] {});

		LOGGER.info("Infos");
		LOGGER.info("Name: {}", instantiateProposalRequest.getChaincodeName());
		LOGGER.info("Version: {}", instantiateProposalRequest.getChaincodeID().getVersion());
		LOGGER.info("Fcn: {}", instantiateProposalRequest.getFcn());
		for (String args : instantiateProposalRequest.getArgs())
			LOGGER.info("Args: {}", args);

		Map<String, byte[]> tm = new HashMap<>();
		tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
		tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
		instantiateProposalRequest.setTransientMap(tm);

		if (chaincode.getEndorsement() != null) {
			LOGGER.info("Using EndorsementPolicy");
			String pathEndorsament = "./endorsementypolicy/" + chaincode.getEndorsement() + ".yaml";
			ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
			chaincodeEndorsementPolicy.fromYamlFile(new File(pathEndorsament));
			instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
			LOGGER.info("Open {}", pathEndorsament);
		} else
			LOGGER.info("Without EndorsementPolicy");

		proposalSuccessful.clear();
		proposalFailed.clear();

		proposalResponses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
		LOGGER.info("Get response");
		for (ProposalResponse response : proposalResponses) {
			LOGGER.info("Peer {}", response.getPeer().getName());
			LOGGER.info("Status {}", response.getStatus());
			LOGGER.info("Message {}", response.getMessage());
			LOGGER.info("Transaction Id {}", response.getTransactionID());
			LOGGER.info("{}", response.isVerified());

			responseInstantiateChaincode.add(new ResponsePeer(response.getPeer().getName(),
					response.getStatus().toString(), response.getMessage(), response.getTransactionID()));
			if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
				proposalSuccessful.add(response);
			} else {
				proposalFailed.add(response);
			}
		}

		LOGGER.info("Erros: {}", proposalFailed.size());

		return sendTransactionSync(responseInstantiateChaincode, channel, proposalResponses);
	}

	public List<Response> upgradeChaincode(HFClient client, Channel channel, ChaincodeMaintenance chaincode)
			throws ProposalException, InvalidArgumentException, ChaincodeEndorsementPolicyParseException, IOException {
		List<Response> responseUpgradeChaincode = new ArrayList<>();

		LOGGER.info("Start Upgrade chaincode");
		LOGGER.info("Create UpgradeProposalRequest");
		LOGGER.info("Object {}", chaincode.toString());

		UpgradeProposalRequest upgradeProposalRequest = client.newUpgradeProposalRequest();
		upgradeProposalRequest.setChaincodeID(chaincode.getChaincodeID());
		upgradeProposalRequest.setChaincodeVersion(chaincode.getVersion());
		upgradeProposalRequest.setChaincodeLanguage(Type.GO_LANG);
		upgradeProposalRequest.setFcn("init");
		upgradeProposalRequest.setArgs(new String[] {});

		LOGGER.info("Infos");
		LOGGER.info("Name: {}", upgradeProposalRequest.getChaincodeName());
		LOGGER.info("Version: {}", upgradeProposalRequest.getChaincodeID().getVersion());
		LOGGER.info("Fcn: {}", upgradeProposalRequest.getFcn());
		for (String args : upgradeProposalRequest.getArgs())
			LOGGER.info("Args: {}", args);

		if (chaincode.getEndorsement() != null) {
			LOGGER.info("Using EndorsementPolicy");
			String pathEndorsament = "./endorsementypolicy/" + chaincode.getEndorsement() + ".yaml";
			ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
			chaincodeEndorsementPolicy.fromYamlFile(new File(pathEndorsament));
			upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
			LOGGER.info("Open {}", pathEndorsament);
		} else
			LOGGER.info("Without EndorsementPolicy");

		proposalResponses = channel.sendUpgradeProposal(upgradeProposalRequest);
		LOGGER.info("Get response");
		for (ProposalResponse response : proposalResponses) {
			LOGGER.info("Peer {}", response.getPeer().getName());
			LOGGER.info("Status {}", response.getStatus());
			LOGGER.info("Message {}", response.getMessage());
			LOGGER.info("Transaction Id {}", response.getTransactionID());

			responseUpgradeChaincode.add(new ResponsePeer(response.getPeer().getName(), response.getStatus().toString(),
					response.getMessage(), response.getTransactionID()));
			if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
				proposalSuccessful.add(response);
			} else {
				proposalFailed.add(response);
			}
		}

		LOGGER.info("Erros: {}", proposalFailed.size());
		
		return sendTransactionSync(responseUpgradeChaincode, channel, proposalResponses);
	}

	public List<Response> invoke(HFClient client, Channel channel, ChaincodeFeatures chaincode)
			throws ProposalException, InvalidArgumentException, InterruptedException, ExecutionException {
		List<Response> responseInvokeChaincode = new ArrayList<>();

		LOGGER.info("Start invokeTest chaincode");
		LOGGER.info("Create TransactionProposalRequest");
		LOGGER.info("Object {}", chaincode.toString());
		LOGGER.info("ChaincodeName {}", chaincode.getChaincodeName());

		TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
		ChaincodeID cid = ChaincodeID.newBuilder().setName(chaincode.getChaincodeName()).build();
		transactionProposalRequest.setChaincodeID(cid);
		transactionProposalRequest.setFcn(chaincode.getMethod());
		transactionProposalRequest.setArgs(chaincode.getArgs());

		LOGGER.info("Infos");
		LOGGER.info("Name: {}", transactionProposalRequest.getChaincodeName());
		LOGGER.info("Version: {}", transactionProposalRequest.getChaincodeID().getVersion());
		LOGGER.info("Fcn: {}", transactionProposalRequest.getFcn());
		for (String args : transactionProposalRequest.getArgs())
			LOGGER.info("Args: {}", args);

		LOGGER.info("Channel {}", channel.getName());
		LOGGER.info("Status {}", channel.isInitialized());

		proposalResponses = channel.sendTransactionProposal(transactionProposalRequest);
		LOGGER.info("Get response");
		for (ProposalResponse response : proposalResponses) {
			LOGGER.info("Peer {}", response.getPeer().getName());
			LOGGER.info("Status {}", response.getStatus());
			LOGGER.info("Message {}", response.getMessage());
			LOGGER.info("Transaction Id {}", response.getTransactionID());

			responseInvokeChaincode.add(new ResponsePeer(response.getPeer().getName(), response.getStatus().toString(),
					response.getMessage(), response.getTransactionID()));
		}

		LOGGER.info("Send Transaction");
		
		return sendTransactionSync(responseInvokeChaincode, channel, proposalResponses);
	}

	public List<Response> query(HFClient client, Channel channel, ChaincodeFeatures chaincode)
			throws InvalidArgumentException, ProposalException, JsonParseException, JsonMappingException, IOException {
		List<Response> responseQueryTest = new ArrayList<>();
		ResponsePeer responsePeer;
		String stringResponse = "";

		LOGGER.info("Start query chaincode");
		LOGGER.info("Create QueryByChaincodeRequest");
		LOGGER.info("Object {}", chaincode.toString());

		QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
		ChaincodeID cid = ChaincodeID.newBuilder().setName(chaincode.getChaincodeName()).build();
		queryByChaincodeRequest.setChaincodeID(cid);
		queryByChaincodeRequest.setFcn(chaincode.getMethod());
		queryByChaincodeRequest.setArgs(chaincode.getArgs());
		LOGGER.info("Infos");
		LOGGER.info("Name: {}", queryByChaincodeRequest.getChaincodeName());
		LOGGER.info("Version: {}", queryByChaincodeRequest.getChaincodeID().getVersion());
		LOGGER.info("Fcn: {}", queryByChaincodeRequest.getFcn());
		for (String args : queryByChaincodeRequest.getArgs())
			LOGGER.info("Args: {}", args);

		Collection<ProposalResponse> response = channel.queryByChaincode(queryByChaincodeRequest);
		LOGGER.info("Get response");
		for (ProposalResponse proposalResponse : response) {
			LOGGER.info("status {}", proposalResponse.getStatus());
			LOGGER.info("message {}", proposalResponse.getMessage());
					
			stringResponse = new String(proposalResponse.getChaincodeActionResponsePayload());
			LOGGER.info("Response {}", stringResponse);
			responsePeer = new ResponsePeer(proposalResponse.getPeer().getName(),
					proposalResponse.getStatus().toString(), proposalResponse.getMessage(),
					proposalResponse.getTransactionID());
			
			responseQueryTest.add(new ResponseQuery(responsePeer, mapper.readValue(stringResponse, Object.class)));

			//LOGGER.info("Response: {}", stringResponse);
		}

		return responseQueryTest;
	}
	
	private List<Response> sendTransactionSync(List<Response> responseProposal, Channel channel, Collection<ProposalResponse> proposalResponses){
		try {
			TransactionEvent transactionEvent = channel.sendTransaction(proposalResponses).get();
			LOGGER.info("Size: {}", transactionEvent.getTransactionActionInfoCount());
			for (TransactionActionInfo transaction : transactionEvent.getTransactionActionInfos()) {
				LOGGER.info("Status: {}", transaction.getResponseStatus());
				LOGGER.info("Message: {}", transaction.getResponseMessage());
				LOGGER.info("Payload: {}", new String(transaction.getEvent().getPayload()), StandardCharsets.UTF_8);
				LOGGER.info("Status final: {}",	new String(transaction.getProposalResponsePayload(), StandardCharsets.UTF_8));

				responseProposal.add(new ResponseSdk(String.valueOf(transaction.getResponseStatus()),
						transaction.getResponseMessage(),
						new String(transaction.getEvent().getPayload(), StandardCharsets.UTF_8),
						new String(transaction.getProposalResponsePayload(), StandardCharsets.UTF_8)));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return responseProposal;
	}

}
