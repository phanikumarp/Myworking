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
location=`pwd`
#`cat /dev/null > $location/status.txt`
declare -a services=("mysql")
while true
do
        current_time= echo `date`
        for line in "${services[@]}"
        do
                now=$(($(date +%s%N)/1000000000))
                out_s=`ps aux | grep $line | grep -v "grep"`
                value=0
                if [ "$out_s" != "" ]
                then
                        value=1
                fi
                echo "put mysql.status $now $value host=$hostname service=$line" | nc -w 30 $tsdbhost $tsdbport
                echo $current_time
                echo "put mysql.status $now $value host=$hostname service=$line"
        done
        sleep $sleepintervel
done

