#!/bin/sh
docker stop summer-iwebot
docker rm summer-iwebot
docker rmi summer-iwebot
docker build -t summer-iwebot .
docker run --name summer-iwebot --privileged=true --restart always --network=host -d summer-iwebot
