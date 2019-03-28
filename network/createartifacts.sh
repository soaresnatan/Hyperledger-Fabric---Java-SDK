#!/bin/bash

./configtxgen -profile orderergenesis -outputBlock ./channel-artifacts/genesis.block
./configtxgen -profile channelone -outputCreateChannelTx ./channel-artifacts/channelone.tx -channelID channelone
./configtxgen -profile channelone -outputAnchorPeersUpdate ./channel-artifacts/org1-channeloneMSPanchors.tx -channelID channelone -asOrg org1
./configtxgen -profile channelone -outputAnchorPeersUpdate ./channel-artifacts/org2-channeloneMSPanchors.tx -channelID channelone -asOrg org2
./configtxgen -profile channeltwo -outputCreateChannelTx ./channel-artifacts/channeltwo.tx -channelID channeltwo
./configtxgen -profile channeltwo -outputAnchorPeersUpdate ./channel-artifacts/org1-channeltwoMSPanchors.tx -channelID channeltwo -asOrg org1
./configtxgen -profile channeltwo -outputAnchorPeersUpdate ./channel-artifacts/org2-channeltwoMSPanchors.tx -channelID channeltwo -asOrg org2
./configtxgen -profile channeltree -outputCreateChannelTx ./channel-artifacts/channeltree.tx -channelID channeltree
./configtxgen -profile channeltree -outputAnchorPeersUpdate ./channel-artifacts/org1-channeltreeMSPanchors.tx -channelID channeltree -asOrg org1
./configtxgen -profile channeltree -outputAnchorPeersUpdate ./channel-artifacts/org2-channeltreeMSPanchors.tx -channelID channeltree -asOrg org2
./configtxgen -profile channeltree -outputAnchorPeersUpdate ./channel-artifacts/org3-channeltreeMSPanchors.tx -channelID channeltree -asOrg org3
