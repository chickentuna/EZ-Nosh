#!/bin/bash
set -e

BUILDX_NO_DEFAULT_ATTESTATIONS=1 docker compose build
docker compose push

ssh poulton "cd ~/ez-nosh && docker-compose pull && docker-compose down && docker-compose up -d"
