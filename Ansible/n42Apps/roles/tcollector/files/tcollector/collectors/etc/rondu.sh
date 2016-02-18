#!/bin/bash
netstat -natp | grep -i established|awk '{print $5}' > config.txt
