#!/bin/bash

(cd ./db/; sbt "neo4jLoadDependencies")
(cd ./services; sbt "neo4jLoadDependencies")

