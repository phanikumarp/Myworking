#!/bin/bash
tsdb=175.126.104.46
tsdbport=4343
hostname=`hostname`
location=`pwd`
appName=AlgeriaOpenstack
#`cat /dev/null > $location/status.txt`
declare -a services=("mysql")
while true
do
for line in "${services[@]}"
do
   now=$(($(date +%s%N)/1000000000))
   out_s=`ps aux | grep $line | grep -v "grep"`
   value=0
   if [ "$out_s" != "" ]
   then
   value=1
   fi
   echo "put mysql.status $now $value host=$hostname service=$line appname=$appName" | nc -w 30 $tsdb $tsdbport
   echo "put mysql.status $now $value host=$hostname service=$line appname=$appName"
done
sleep 60
done
