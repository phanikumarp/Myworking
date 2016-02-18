#!/bin/bash
. /root/keystonerc_admin

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
hostname=`hostname`

function centos()
{
echo "Current Machine has RedHat based Operating System"
while true;do
	openstack-status | grep "neutron-\|openvswitch\|nova-\|glance-\|horizon-\|swift-\|ceilometer-\|cinder-\|rabbitmq-\|libvirtd\|dbus" | grep -v "disabled on boot" | while read line;do
		check=`echo $line | cut -d" " -f1`
		if [ "$check" == "|" ]
		then
			continue
		fi
		service=`echo $line | cut --d=":" -f1`
		value=`echo $line | cut --d=":" -f2 | cut --d=" " -f2`
		now=$(($(date +%s%N)/1000000000))
		status_string="active"
		if [ "$value" == "active" ];
		then
			value=1
		else
			value=0
		fi
		echo "put $service.status $now $value host=$hostname service=$service" | nc -w 30 $tsdbhost $tsdbport
		echo "$service.status $now $value host=$hostname service=$service"
	done
	sleep $sleepintervel
done
}

function ubuntu()
{
echo "Current Machine has Debian based Operating System"
while true;do
	location=`pwd`
	declare -a services=("rabbitmq-server" "keystone" "glance-api" "glance-registry" "nova-api" "nova-cert" "nova-consoleauth" "nova-scheduler" "nova-compute" "nova-novncproxy" "neutron-server" "openvswitch-switch" "neutron-plugin-openvswitch-agent" "neutron-l3-agent" "neutron-dhcp-agent" "neutron-metadata-agent" "cinder-api" "cinder-scheduler" "cinder-volume" "apache2" "heat-api" "heat-api-cfn" "heat-engine" "swift-proxy" "swift-account" "swift-container" "swift-object" "ceilometer-agent-central" "ceilometer-agent-notification" "ceilometer-api" "ceilometer-collector" "ceilometer-alarm-evaluator" "ceilometer-alarm-notifier" "ceilometer-agent-compute" "docker")
	for line in "${services[@]}"
	do
		now=$(($(date +%s%N)/1000000000))
   		dpkg --get-selections | grep -v deinstall | grep -w $line > /dev/null
   		if [ $? == 0 ]
		then
       		service $line status > /dev/null
       		if [ $? == 0 ]
       		then
           		value=1
           		echo "put openstack.$line.status $now $value host=$hostname service=$line " | nc -w 30 $tsdbhost $tsdbport
           		echo "openstack.$line.status $now $value host=$hostname service=$line "
       		else
           		value=0
           		echo "put openstack.$line.status $now $value host=$hostname service=$line " | nc -w 30 $tsdbhost $tsdbport
           		echo "openstack.$line.status $now $value host=$hostname service=$line "
       		fi
   		fi
	done
	sleep $sleepintervel
done
}


if [ -f /etc/debian_version ]; 
then
	ubuntu	    
elif [ -f /etc/redhat-release ]; 
then
	centos
fi
