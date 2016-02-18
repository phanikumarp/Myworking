#!/bin/bash
tsdb=175.126.104.46
tsdbport=4343
host=`hostname`
appName=AlgeriaOpenstack

sudo rabbitmqctl list_queues | awk '/^dhcp_agent\..*\t|^l3_agent\..*\t|^q-firewall-plugin\..*\t|^q-l3-plugin\..*\t|^q-plugin\..*\t|^compute\..*\t|^conductor\..*\t|^consoleauth\..*\t|^scheduler\..*\t|^cert\..*\t|^cinder-scheduler\..*\t|^cinder-volume\..*\t/' | while read var3;do
                now=$(($(date +%s%N)/1000000000))
                service=`echo $var3 | cut --d=" " -f1 | cut --d="." -f1`
                targethost=`echo $var3 | cut --d=" " -f1 | cut --d="." -f2`
                service_value=`echo $var3 | cut --d=" " -f2`
                echo "put $service.queue.len $now $service_value host=$host targethost=$targethost" | nc -w 30 $tsdb $tsdbport
                echo "$service.queue.len $now $service_value host=$host targethost=$targethost"

done

sleep 60

