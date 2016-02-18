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
host=`hostname`

declare -a services=("keystone" "glance" "nova" "neutron" "cinder" "heat" "ceilometer" "swift")
declare -a urls=("http://172.25.30.2:5000/v2.0" "http://172.25.30.2:9292" "http://172.25.30.2:8774" "http://172.25.30.2:9696" "http://172.25.30.2:8776" "http://172.25.30.2:8004" "http://172.25.30.2:8777" "http://172.25.30.2:8080")

while true
do
current_time= echo `date`
counter=-1
for url in "${urls[@]}"
do
   counter=$counter+1
   now=$(($(date +%s%N)/1000000000))
   curl -Is $url > /dev/null
   output=`echo $?`
   if [ $output == '0' ]
   then
   	value=1
   else
	value=0
   fi
   echo "put url.accessible $now $value host=$host endpoint=${services[$counter]}" | nc -w 30 $tsdbhost $tsdbport || echo $current_time
   echo "put url.accessible $now $value host=$host endpoint=${services[$counter]}"
done
sleep 60
done
