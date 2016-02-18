#!/bin/bash
base_path=`pwd`
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
done < $base_path/health_scoring/hostconf.txt
echo $tsdbhost
sleepintervel=60
hostname=`hostname`
while true
do
current_time= echo `date`
ip link show | grep -v "link/" | while read line;do
	ifname=`echo $line | cut -d " " -f2 | cut -d ":" -f1`
	ifstate=`cat /sys/class/net/$ifname/operstate`
        now=$(($(date +%s%N)/1000000000))
        if [ "$ifstate" = "up" ];
        then
                value=1
        else
                value=0
        fi
        echo "put net.interface.status $now $value host=$hostname interface=$ifname" | nc -w 30 $tsdbhost $tsdbport || echo $current_time
        echo "net.interface.status $now $value host=$hostname interface=$ifname"
done
sleep $sleepintervel
done
