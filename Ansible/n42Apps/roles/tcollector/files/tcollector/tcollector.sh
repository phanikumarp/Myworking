#!/bin/bash

# 
# description: tcollector 
#
# Get function from functions library
# Start the tcollector

SCRIPT_DIR="$(cd $(dirname $0) && pwd)"
TCOLLECTOR_PATH=${TCOLLECTOR_PATH-"${SCRIPT_DIR}"}
PROG=$TCOLLECTOR_PATH/tcollector.py

start_t() {
        echo -n Starting tcollector: 
         	ps aux | grep -v "grep" | grep "tcollector" > /dev/null
		if [ $? = 0 ]
                   then
			$TCOLLECTOR_PATH/startstop start 
                        exit 0
		   else
			echo -n tcollector service is already running
         #               echo
			exit 1	
		fi  
}
# Restart the Agent
stop_t() {
        echo -n Stopping tcollector service: 
       $TCOLLECTOR_PATH/startstop stop
      PID=`ps -ef | grep -v "grep" | grep "$PROG" | awk 'NR==1{print $2}' | cut -d' ' -f1`
      #echo
      if [ $PID ] > /dev/null
      then
        echo $PID
        kill -9 $PID
      fi
        exit 0
}
# Status of Agent
status_t () {
      pid=`ps aux | grep -v "grep" | grep "$PROG -c"` >/dev/null
      if [ $? = 0 ];then
          echo "$PROG" running
          exit 0
	else 
	  echo "$PROG" not running
	exit 1
       fi
}    
    		
### main logic ###
case "$1" in
  start)
        start_t
        ;;
  stop)
        stop_t
        ;;
  status)
	status_t
        ;;
  restart)
        stop_t
        start_t
        ;;
  *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
esac
exit 0
