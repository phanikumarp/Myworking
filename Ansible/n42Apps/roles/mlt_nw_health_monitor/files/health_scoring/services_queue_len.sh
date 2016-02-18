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
done < `pwd`/health_scoring/hostconf.txt

sleepintervel=60
host=`hostname`
while true
do
current_time= echo `date`
rabbitmqctl list_queues | awk '/^dhcp_agent\..*\t|^l3_agent\..*\t|^q-firewall-plugin\..*\t|^q-l3-plugin\..*\t|^q-plugin\..*\t|^compute\..*\t|^conductor\..*\t|^consoleauth\..*\t|^scheduler\..*\t|^cert\..*\t|^cinder-scheduler\..*\t|^cinder-volume\..*\t/' | while read var3;do
                now=$(($(date +%s%N)/1000000000))
                service=`echo $var3 | cut --d=" " -f1 | cut --d="." -f1`
                targethost=`echo $var3 | cut --d=" " -f1 | cut --d="." -f2`
                service_value=`echo $var3 | cut --d=" " -f2`
                echo "put $service.queue.len $now $service_value host=$host targethost=$targethost" | nc -w 30 $tsdbhost $tsdbport || echo $current_time
                echo "$service.queue.len $now $service_value host=$host targethost=$targethost"
done
sleep 2
done
