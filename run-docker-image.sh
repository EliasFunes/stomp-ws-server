#!/bin/bash

docker run -d \
--name server \
-p 8080:8080 \
--env-file .env \
stomp-ws-server:latest