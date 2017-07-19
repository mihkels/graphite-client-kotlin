#!/usr/bin/env bash

PORT=2003
SERVER=localhost
echo "local.random.diceroll 4 `date +%s`" | nc -q0 ${SERVER} ${PORT}
