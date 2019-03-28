#!/bin/bash

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

docker-compose -f docker-composer.yaml up -d
sleep 10s

chmod +x ./channel-artifacts/channels.sh
docker exec network_fabric-cli_1 bash ./channel-artifacts/channels.sh


docker run -d --name="logspout"  --volume=/var/run/docker.sock:/var/run/docker.sock --publish=127.0.0.1:5000:80 --network networkkafka_default gliderlabs/logspout
sleep 3
curl http://127.0.0.1:5000/logs
