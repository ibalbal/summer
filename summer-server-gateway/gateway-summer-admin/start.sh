#!/bin/sh
docker stop summer-gateway
docker rm summer-gateway
docker rmi summer-gateway
docker build -t summer-gateway .
docker run --name summer-gateway --privileged=true --restart always --network=host -d summer-gateway
