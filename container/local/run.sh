echo "Starting Docker Compose services..."
docker rm $(docker ps -aq) -f
docker compose -f docker-compose.yml up -d