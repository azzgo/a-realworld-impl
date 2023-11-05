#!/bin/bash


function run() {
  POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-password}
  docker run --name postgress-local -p 5432:5432 \
    -e POSTGRES_USER=sa \
    -e POSTGRES_DB=conduit \
    -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD} -d postgres:16.0-bullseye
}

function boot() {
  docker restart postgress-local
}

$1
