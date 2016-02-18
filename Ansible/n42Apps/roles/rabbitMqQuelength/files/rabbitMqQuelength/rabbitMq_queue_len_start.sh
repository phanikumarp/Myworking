#!/bin/bash
#
# chkconfig: 35 90 12
# description: VM Security Rules Change Agent service
#
# Get function from functions library
# . /etc/init.d/functions
. /lib/lsb/init-functions
# Start the Agent
start() {
        echo -n `date` Starting Rabbit Queue Agent: 
		ps aux | grep -v "grep" | grep "rabbitMq_queue_len.sh" > /dev/null
		if [ $? != 0 ]
			then
				nohup sh rabbitMq_queue_len.sh > rabbitMqQuelength.out 2>&1 &
				### Create the lock file ###
				log_success_msg $"`date` RabbitMq Queue Agent service startup"
				exit 0
			else
				echo 
				echo -n `date` RabbitMq Queue Agent service is already running
				exit 1
				
		fi   
}
# Restart the Agent
stop() {
        echo -n `date` Stopping RabbitMq Queue Agent service: 
        PID=`ps -ef | grep "rabbitMq_queue_len.sh" | grep -v grep | awk '{ print $2 }'`
		kill -9 $PID
        ### Now, delete the lock file ###
		log_success_msg $"`date` RabbitMq Queue Agent service Stopped"
        exit 0
}
# Status of Agent
status() {
        ps aux | grep -v "grep" | grep "rabbitMq_queue_len.sh" > /dev/null
		if [ $? != 0 ]
			then 
				echo "`date` RabbitMq Queue Agent is not running";
				exit 1
			else 
				echo "`date` RabbitMq Queue Agent is Running";
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
