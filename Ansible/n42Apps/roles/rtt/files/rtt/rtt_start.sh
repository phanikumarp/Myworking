#!/bin/bash
#
# chkconfig: 35 90 12
# description: RTT Agent service
#
# Get function from functions library
# . /etc/init.d/functions
. /lib/lsb/init-functions
# Start the Agent
start() {
        echo -n `date` Starting RttAgent: 
		ps aux | grep -v "grep" | grep "nohup_rtt.sh" > /dev/null
		if [ $? != 0 ]
			then
				nohup sh nohup_rtt.sh > /dev/null 2>&1 &
				### Create the lock file ###
				log_success_msg $"`date` RttAgent service startup"
				exit 0
			else
				echo
				echo -n `date` RttAgent service is already running
				exit 1
				
		fi   
}
# Restart the Agent
stop() {
        echo -n `date` Stopping RttAgent service: 
        PID=`ps -ef | grep nohup_rtt.sh | grep -v grep | awk '{ print $2 }'`
        if [ $PID ] > /dev/null
          then
           #echo $PID
          kill -9 $PID
        fi
        PID=`ps -ef | grep rtt.py | grep -v grep | awk '{ print $2 }'`
         if [ $PID ] > /dev/null
           then
           kill -9 $PID
         fi
        ### Now, delete the lock file ###
	   log_success_msg $"`date` RttAgent service Stopped"
        exit 0
}
# Status of Agent
status() {
        ps aux | grep -v "grep" | grep "nohup_rtt.sh" > /dev/null
		if [ $? != 0 ]
			then 
				echo "`date` rtt agent is not running";
				exit 1
			else 
				echo "`date` rtt agent is Running";
				exit 0
		fi
		
}
### main logic ###
case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  status)
	status
        ;;
  restart)
        stop
        start
        ;;
  *)
        echo $"`date` Usage: $0 {start|stop|restart|status}"
        exit 1
esac
exit 0
