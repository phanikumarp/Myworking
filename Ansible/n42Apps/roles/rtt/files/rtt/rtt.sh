#!/bin/bash
#ss -in | grep "tcp\|rtt" | grep -v "ipproto" | while read line1; do
#ss -ip | grep "tcp\|rtt\|neutron-l3-agen" | grep -v "ipproto" | sed '1d' | while read line1; do
ss -ip | grep "tcp\|rtt" | grep -v "ipproto" | grep -v "SYN-SENT" | while read line1; do
    read line2
    if [[ $line1 == "tcp"* ]] && [[ $line2 == "cubic"* ]]
    then
        l1=$line1
        l2=$line2
    else
        l1=$line2
        l2=$line1
    fi

    src=`echo $l1 | cut -d' ' -f5 | grep -E -o "([0-9]{1,3}[\.]){3}[0-9]{1,3}"`
    dst=`echo $l1 | cut -d' ' -f6 | grep -E -o "([0-9]{1,3}[\.]){3}[0-9]{1,3}"`
    dst_ser=`echo $l1 | cut -d' ' -f6 | grep -E -o "([\.][0-9]{1,3}[\:][a-z,A-Z]{1,15})" | cut -d':' -f2`
    src_ser=`echo $l1 | cut -d' ' -f7 | grep -E -o "(([\"])[a-z,A-Z,0-9\-]{1,15}([\"]))" | cut -d'"' -f2 | cut -d'"' -f1`
 
    rtt=`echo $l2 | cut -d' ' -f4 | cut -d':' -f2 | cut -d'/' -f1`
    rtt_var=`echo $l2 | cut -d' ' -f4 | cut -d':' -f2 | cut -d'/' -f2`
        
    if [ -z "$src_ser" ];
    then
        src_ser="No-service"
    fi

    if [ -z "$dst_ser" ];
    then
        dst_ser="No-service"
    fi
        
    if [ $src != $dst ];
    then
        echo $src $src_ser $dst $dst_ser $rtt $rtt_var
    fi
done

