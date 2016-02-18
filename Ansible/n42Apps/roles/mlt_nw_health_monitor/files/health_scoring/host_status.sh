#!/bin/bash

## reading conf values
while IFS='=' read -r key kval
do
  if [ "$key" =  "tsdbhost" ]
  then
     tsdbhost="$kval"
  elif [ "$key" = "tsdbport" ]
  then
     tsdbport="$kval"
  else [ "$key" = "appname" ]
     appname="$kval"
  fi
done < hostconf.txt
sleepintervel=60
hostname=`hostname`

while true
do
now=$(($(date +%s%N)/1000000000))
value=1
echo "put host.status $now $value host=$hostname" | nc -w 30 $tsdhost $tsdbport 
echo "host.status $now $value host=$hostname"
sleep $sleepintervel
done
