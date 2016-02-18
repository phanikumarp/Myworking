#!/bin/bash
#Author:	  Veerendra.K
#Description: Starts the Agents, for help type './masterAgent.sh'	

declare -A agents=(
["rtt"]="stdbuf -oL python `pwd`/rtt/rtt.py"
["multilyer_network_health"]="stdbuf -oL python `pwd`/multi_layer_network_health_monitor/multilyer_network_health.py"
["storage_comp_mem"]="bash `pwd`/multi_layer_network_health_monitor/storage_comp_mem.sh"
["tenant"]="sudo sh `pwd`/tenant/tenant.sh"
["host_status"]="bash `pwd`/health_scoring/host_status.sh"
["interface_status"]="bash `pwd`/health_scoring/interface_status.sh"
["mysql_status"]="bash `pwd`/health_scoring/mysql_status.sh"
["services_queue_len"]="bash `pwd`/health_scoring/services_queue_len.sh"
["url_test"]="bash `pwd`/health_scoring/url_test.sh"
["openstack_services_status"]="bash `pwd`/health_scoring/openstack_services_status.sh"
["ovs_status"]="stdbuf -oL python `pwd`/health_scoring/ovs_status.py")

function start_scripts()
{
	if [[ "$2" = "log" ]]
	then
		log="`pwd`/logs/\$tmp.log"
	else
		log="/dev/null"
	fi
	
	if [[ ${agents[$1]} ]]
	then
		temp=${agents[$1]}
		unset $agents
		declare -A agents=(["$1"]="$temp")	
	elif [ "$1" != "all" ]
	then	
		echo "The given Agent '$1' was't found!"
		help
		exit 1
	fi
	for tmp in ${!agents[@]}
	do
		pid=`ps -ef | grep $tmp | grep -v grep | grep -v "masterAgent" | awk '{print $2}'`
		if [[ $pid =~ [0-9] ]]
		then
			echo "$tmp is Already Running"
		else
			${agents[$tmp]} >> `eval "echo $log"` 2>&1 &
			echo "$tmp Agent started!"
		fi
	done
}


function status()
{
	if [[ ${agents[$1]} ]]
	then
		temp=${agents[$1]}
		unset $agents
		declare -A agents=(["$1"]="$temp")	
	elif [ "$1" != "all" ]
	then	
		echo "The given Agent '$1' was't found"	
		help
		exit 1
	fi
	for tmp in ${!agents[@]}
	do
		pid=`ps -ef | grep $tmp | grep -v grep | grep -v "masterAgent" | awk '{print $2}'`
		if [[ $pid =~ [0-9] ]]
		then
			echo "$tmp is Running"
		else
			echo "$tmp is Not Running"
		fi
	done
}



function stop()
{
	if [[ ${agents[$1]} ]]
	then
		temp=${agents[$1]}
		unset $agents
		declare -A agents=(["$1"]="$temp")	
	elif [ "$1" != "all" ]
	then	
		echo "The given Agent '$1' was't found"	
		help
		exit 1
	fi
	
	for tmp in ${!agents[@]}
	do
		sudo kill -9 $(ps -ef | grep $tmp | grep -v grep | grep -v "masterAgent" | awk '{print $2}') 2>/dev/null
		echo "Logs Cleared! `date`" > `pwd`/logs/$tmp.log
		echo "$tmp Stopped!"
	done
}

function clear_logs()
{
ls `pwd`/logs | while read file;do
	echo "" > `pwd`/logs/$file 
done	
}

function help()
{
	echo ""
	echo "******************************HELP*************************************"
	echo "Usage: {start|stop|restart|status|clear}"
	echo "To start all agents without logs--> $./masterAgent.sh start all"
	echo "To start specific agent without logs--> $./masterAgent.sh start <AGENT NAME>"
	echo "To start agents with logs--> $./masterAgent.sh start <AGENT_NAME/ALL> log"
	echo "To clear Agent's log--> $./masterAgent.sh clear"
	echo "The List of Agents given below"
	for a in ${!agents[@]}
	do
		echo "-$a"
	done
	echo ""
}


case "$1" in
  start)
        if [ -z $2 ]
        then
        	echo "Please specify the agent name to start!"
        	help
    	else
        	start_scripts $2 $3
    	fi
        ;;
  stop)
        if [ -z $2 ]
        then
        	echo "Please specify the agent name to stop!"
        	help
    	else
        	stop $2
    	fi 
        ;;
  status)
        if [ -z $2 ]
        then
        	echo "Please specify the agent name to know status!"
        	help
    	else
        	status $2
    	fi
        ;;
  restart)
        if [ -z $2 ]
        then
        	echo "Please specify the agent name to restart!"
        	help
    	else
        	stop $2
        	echo ""
        	start_scripts $2
    	fi
        ;;
	clear)
		clear_logs
		;;
	*)
        help
        exit 1
esac
exit 0
