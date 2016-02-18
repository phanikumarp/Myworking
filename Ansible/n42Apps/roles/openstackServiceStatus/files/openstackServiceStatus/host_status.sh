#!/bin/bash
tsdb=175.126.104.46
tsdbport=4343
hostname=`hostname`
appName=AlgeriaOpenstack
while true
do
now=$(($(date +%s%N)/1000000000))
value=1
echo "put host.status $now $value host=$hostname appname=$appName" | nc -w 30 $tsdb $tsdbport
echo "host.status $now $value host=$hostname appname=$appName"
sleep 60
done
