#!/bin/bash

# Get the version from build.gradle
#version=$(grep -Po '(?<=version ")([0-9]+\.[0-9]+\.[0-9]+)' build.gradle)
#version=$(sed -n "s/.*version = '\([^']*\)'.*/\1/p" build.gradle)
version=`cat build.gradle | grep "version" | cut -d " " -f2 | xargs`


echo "version: $version";

# Build the Gradle project
./gradlew build

# Check if the Gradle build was successful
if [ $? -ne 0 ]; then
    echo "Gradle build failed. Aborting Docker image build."
    exit 1
fi

# Build the Docker image
docker build -t stomp-ws-server:$version .

# Check if the Docker image build was successful
if [ $? -ne 0 ]; then
    echo "Docker image build failed."
    exit 1
fi

# Tag the Docker image build to latest
docker tag stomp-ws-server:$version stomp-ws-server:latest



echo "Docker image built successfully."