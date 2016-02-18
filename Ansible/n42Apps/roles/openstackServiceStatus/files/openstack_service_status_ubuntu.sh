#!/bin/bash
tsdb=52.8.104.253
tsdbport=4343
hostname=`hostname`
appName=AlgeriaOpenstack
now=$(($(date +%s%N)/1000000000))
location=`pwd`
#`cat /dev/null > $location/status.txt`
declare -a services=("rabbitmq-server" "keystone" "glance-api" "glance-registry" "nova-api" "nova-cert" "nova-consoleauth" "nova-scheduler" "nova-compute" "nova-conductor" "nova-novncproxy" "dbus" "neutron-server" "openvswitch-switch" "neutron-plugin-openvswitch-agent" "neutron-l3-agent" "neutron-dhcp-agent" "neutron-metadata-agent" "cinder-api" "cinder-scheduler" "cinder-volume" "apache2" "memcached"  "heat-api" "heat-api-cfn" "heat-engine" "swift-proxy" "swift-account" "swift-container" "swift-object" "mongodb" "ceilometer-agent-central" "ceilometer-agent-notification" "ceilometer-api" "ceilometer-collector" "ceilometer-alarm-evaluator" "ceilometer-alarm-notifier" "ceilometer-agent-compute")


for line in "${services[@]}"
do

   dpkg --get-selections | grep -v deinstall | grep -w $line > /dev/null
   if [ $? == 0 ]
   then

       service $line status > /dev/null
       if [ $? == 0 ]
       then
           value=1
           echo "put openstack.$line.status $now $value host=$hostname service=$line appname=$appName" | nc -w 30 $tsdb $tsdbport
           echo "openstack.$line.status $now $value host=$hostname service=$line appname=$appName"
       else
           value=0
           echo "put openstack.$line.status $now $value host=$hostname service=$line appname=$appName" | nc -w 30 $tsdb $tsdbport
           echo "openstack.$line.status $now $value host=$hostname service=$line appname=$appName"
       fi

   fi
done
