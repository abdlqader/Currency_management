#!/bin/bash
VERSION=$(date +%Y%m%d%H%M%S)
export VERSION

echo "Building for version: $VERSION"

./mvnw clean install

# build the docker image
docker build -t sanad .

# Tag the image to the repository
docker tag sanad sanad:$VERSION

cd ./container/local || exit
bash run.sh

docker logs app -f