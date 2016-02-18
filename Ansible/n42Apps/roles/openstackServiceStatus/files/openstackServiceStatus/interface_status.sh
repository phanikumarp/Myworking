#!/bin/bash
tsdb=175.126.104.46
tsdbport=4343
hostname=`hostname`
appName=AlgeriaOpenstack
while true
do
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
        echo "put net.interface.status $now $value host=$hostname interface=$ifname appname=$appName" | nc -w 30 $tsdb $tsdbport
        echo "net.interface.status $now $value host=$hostname interface=$ifname appname=$appName"
done
sleep 60
done
