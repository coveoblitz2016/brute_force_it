#!/bin/bash

hg pull
hg up default
mvn package

echo ""
echo ""

echo "RUN THIS:"
echo "java -jar target/client-1.3.0-SNAPSHOT-runnable.jar 87qj94sj <TRAINING/COMPETITION> <gameid>"