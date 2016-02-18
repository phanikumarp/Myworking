#!/bin/bash 
MaxFileSize=10000000
#Max file size 10MB

DIR="$( cd "$( dirname "$0" )" && pwd )"
while true
do
    python $DIR/rtt.py >> $DIR/rtt.log 
    sleep 60
    com=`du -b $DIR/rtt.log`
    file_size=`echo $com | cut -d' ' -f1`
    if [[ "$file_size" -gt "$MaxFileSize" ]]
    then  
        echo ' ' > $DIR/rtt.log
    fi
done
