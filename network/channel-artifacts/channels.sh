#!/bin/bash

#docker run -d --name="logspout"  --volume=/var/run/docker.sock:/var/run/docker.sock --#publish=127.0.0.1:4000:80 --network  networkkafka_default gliderlabs/logspout
#sleep 3
#curl http://127.0.0.1:4000/logs

# CHANNEL ONE
sleep 3s
export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/users/Admin@org1-example-com/msp
export CORE_PEER_ADDRESS=peer0-org1-example-com:7051
export CORE_PEER_LOCALMSPID="org1MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/peers/peer0-org1-example-com/tls/ca.crt

peer channel create -o orderer0-example-com:7050 -c channelone -f ./channel-artifacts/channelone.tx --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example-com/orderers/orderer0-example-com/msp/tlscacerts/tlsca.example-com-cert.pem
peer channel join -b channelone.block
peer channel list 

sleep 1.5s
export CORE_PEER_ADDRESS=peer1-org1-example-com:7051
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/peers/peer1-org1-example-com/tls/ca.crt
peer channel join -b channelone.block
peer channel list 

# CHANNEL TWO
sleep 1.5s
export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/users/Admin@org1-example-com/msp
export CORE_PEER_ADDRESS=peer0-org1-example-com:7051
export CORE_PEER_LOCALMSPID="org1MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/peers/peer0-org1-example-com/tls/ca.crt

peer channel create -o orderer0-example-com:7050 -c channeltwo -f ./channel-artifacts/channeltwo.tx --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example-com/orderers/orderer0-example-com/msp/tlscacerts/tlsca.example-com-cert.pem
peer channel join -b channeltwo.block
peer channel list 

sleep 1s
export CORE_PEER_ADDRESS=peer1-org1-example-com:7051
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/peers/peer1-org1-example-com/tls/ca.crt
peer channel join -b channeltwo.block
peer channel list 

sleep 1s
export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/users/Admin@org2-domain-com/msp
export CORE_PEER_ADDRESS=peer0-org2-domain-com:7051
export CORE_PEER_LOCALMSPID="org2MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/peers/peer0-org2-domain-com/tls/ca.crt
peer channel join -b channeltwo.block
peer channel list 

sleep 1s
export CORE_PEER_ADDRESS=peer1-org2-domain-com:7051
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/peers/peer1-org2-domain-com/tls/ca.crt
peer channel join -b channeltwo.block
peer channel list 

# CHANNEL TREE
sleep 1s
cp -r channel-artifacts/teste/ /opt/gopath/src/github.com/
export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/users/Admin@org1-example-com/msp
export CORE_PEER_ADDRESS=peer0-org1-example-com:7051
export CORE_PEER_LOCALMSPID="org1MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/peers/peer0-org1-example-com/tls/ca.crt

peer channel create -o orderer0-example-com:7050 -c channeltree -f ./channel-artifacts/channeltree.tx --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example-com/orderers/orderer0-example-com/msp/tlscacerts/tlsca.example-com-cert.pem
peer channel join -b channeltree.block
peer channel list 

sleep 1s
export CORE_PEER_ADDRESS=peer1-org1-example-com:7051
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/peers/peer1-org1-example-com/tls/ca.crt
peer channel join -b channeltree.block
peer channel list 

sleep 1s
export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/users/Admin@org2-domain-com/msp
export CORE_PEER_ADDRESS=peer0-org2-domain-com:7051
export CORE_PEER_LOCALMSPID="org2MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/peers/peer0-org2-domain-com/tls/ca.crt
peer channel join -b channeltree.block
peer channel list 

sleep 1s
export CORE_PEER_ADDRESS=peer1-org2-domain-com:7051
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/peers/peer1-org2-domain-com/tls/ca.crt
peer channel join -b channeltree.block
peer channel list 

sleep 1s
export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3-be-com/users/Admin@org3-be-com/msp
export CORE_PEER_ADDRESS=peer0-org3-be-com:7051
export CORE_PEER_LOCALMSPID="org3MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3-be-com/peers/peer0-org3-be-com/tls/ca.crt
peer channel join -b channeltree.block
peer channel list 

sleep 0.5s
export CORE_PEER_ADDRESS=peer1-org3-be-com:7051
export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3-be-com/peers/peer1-org3-be-com/tls/ca.crt
peer channel join -b channeltree.block
peer channel list 

# Path
#cp -r channel-artifacts/teste/ /opt/gopath/src/github.com/

# Install chaincode
#peer chaincode install -n teste -v v1 -p github.com/teste

# Upgrade 
#peer chaincode upgrade -o orderer0-example-com:7050 -C channeltwo -n teste -v v2 -c '{"Args":#["init"]}' --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example-com/orderers/orderer0-example-com/msp/tlscacerts/tlsca.example-com-cert.pem

# Invoke chaincode
#peer chaincode invoke -o orderer0-example-com:7050 -C channeltwo -n teste -c '{"Args":["createAccount","1","1000","Elcius"]}' --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example-com/orderers/orderer0-example-com/msp/tlscacerts/tlsca.example-com-cert.pem

# Query chaincode
#peer chaincode query -o orderer0-example-com:7050 -C channeltwo -n teste -c '{"Args":["getAccountById","1"]}' --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example-com/orderers/orderer0-example-com/msp/tlscacerts/tlsca.example-com-cert.pem

# org1
#export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/users/Admin@org1-example-com/msp
#export CORE_PEER_ADDRESS=peer0-org1-example-com:7051
#export CORE_PEER_LOCALMSPID="org1MSP"
#export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1-example-com/peers/peer0-org1-example-com/tls/ca.crt

# org2
#export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/users/Admin@org2-domain-com/msp
#export CORE_PEER_ADDRESS=peer0-org2-domain-com:7051
#export CORE_PEER_LOCALMSPID="org2MSP"
#export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2-domain-com/peers/peer0-org2-domain-com/tls/ca.crt

# org3
#export CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3-be-com/users/Admin@org3-be-com/msp
#export CORE_PEER_ADDRESS=peer0-org3-be-com:7051
#export CORE_PEER_LOCALMSPID="org3MSP"
#export CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3-be-com/peers/peer0-org3-be-com/tls/ca.crt

