#!/bin/python
'''
Author: Veerendra.K
Description: Will Push the OVS's Data Plane Metrics
'''
import os
import subprocess, time, re, os.path
import potsdb

## reading conf values
with open ("hostconf.txt", "r") as myfile:
    data=myfile.read()
    lines=data.split()
    for item in lines:
      if item.find("tsdbhost") != -1:
          result=item.split("=")
          tsdbhost=(result[1])
      if item.find("tsdbport") != -1:
          result=item.split("=")
          tsdbport=(result[1])
      if item.find("appname") != -1:
          result=item.split("=")
          appname=(result[1])
##### end of the conf reading ...####

metric1 = "net.ovs.hit.percent"
metric2 = "net.ovs.dplost"
metric3 = "net.ovs.status"
metric4 = "net.ovs.rx.drops"
metric5 = "net.ovs.tx.drops"
metric6 = "net.ovs.rx.errors"
metric7 = "net.ovs.tx.errors"
metric8 = "net.ovs.cpu"
metric9 = "net.ovs.mem"
metric10="net.ovs.rx.bytes"
metric11="net.ovs.tx.bytes"

check = None
try:
    if os.path.isfile("/etc/debian_version"):
        print("Current Machine has Debian based Operating System")
        command = "service openvswitch-switch status"
        check = "running"
    elif os.path.isfile("/etc/redhat-release"):
        print("Current Machine has RedHat based Operating System")
        command = "/bin/systemctl status  openvswitch.service | grep Active:"
        check = "active"
    engine_id = subprocess.check_output('sudo ovs-vsctl list netflow | grep engine_id | cut -d":" -f2', shell=True)
    command_dict={
        "ovs_lookups":"ovs-dpctl show | grep lookups",
        "ovs_drop_err":"ovs-ofctl dump-ports br-int | tail -n+2",
        "ovs_status":command,
        "ovs_cpu_mem":"top -b -d1 -n2 | grep 'ovs-' | tail -1"
    }
    
    def find_string(pattern,string):
        match=re.search(pattern,string)
        if match is None:
            return None
        return match.group()
    
    def convert_ovs_port(ovs_port):
        if find_string('[0-9]+', engine_id) is None:
            return ovs_port
        else:
            bin_engine_id=bin(int(engine_id))[2:].zfill(8)[1:]
            bin_port=bin(int(ovs_port))[2:].zfill(16)
            new_port=bin_engine_id+bin_port[7:]
            return int(new_port,2)
    
    def convert_bytes(bytes):
        return round((abs((bytes/1024)/1024)),3)
                        
except Exception, e:
    print "Exception: ",e

while True:
    try:
        metrics = potsdb.Client(tsdbhost, port=tsdbport,qsize=1000, host_tag=True, mps=100, check_host=True)
        lookups1 = subprocess.check_output(command_dict["ovs_lookups"], shell=True)
        ovs_stat_op1=subprocess.check_output(command_dict["ovs_drop_err"],shell=True)
        time.sleep(60)
        lookups2 = subprocess.check_output(command_dict["ovs_lookups"], shell=True)
        ovs_op=subprocess.check_output(command_dict["ovs_status"], shell=True)
        ovs_stat_op2=subprocess.check_output(command_dict["ovs_drop_err"],shell=True)
        ovs_top_op=subprocess.check_output(command_dict["ovs_cpu_mem"],shell=True)
        lookup_list1 = [a.strip() for a in lookups1.split()]
        lookup_list2 = [b.strip() for b in lookups2.split()]
        ovs_stat1 = [d.strip() for d in ovs_stat_op1.split("port")]
        ovs_stat2 = [d.strip() for d in ovs_stat_op2.split("port")]
        ovs_cpu_mem_list = [e.strip() for e in ovs_top_op.split()]
        hit1 = float(lookup_list1[1].split(":")[1])
        hit2 = float(lookup_list2[1].split(":")[1])
        miss1 = float(lookup_list1[2].split(":")[1])
        miss2 = float(lookup_list2[2].split(":")[1])
        lost1 = float(lookup_list1[3].split(":")[1])
        lost2 = float(lookup_list2[3].split(":")[1])
        if hit1 !=hit2 and miss1 != miss2:
            hit_percent = int((abs(hit1-hit2) / (abs(hit2-hit1)+abs(miss1-miss2)))*100)
            metrics.send("net.ovs.hit.percent",hit_percent)
            print metric1,hit_percent
        else:
            print "There was no Cache HITs and Cache Misses for last 1 Minute: So, Hit_Percent will be '101'"
            metrics.send("net.ovs.hit.percent",101)
            print metric1,"101"
        lost = abs(lost1-lost2)
        metrics.send("net.ovs.dplost",lost)
        print metric2,lost
        res = find_string("("+check+")",ovs_op)
        if res:
            ovs_status=1
        else:
            ovs_status=0
        metrics.send("net.ovs.status",int(ovs_status))
        print metric3,ovs_status
        for items1,items2 in zip(ovs_stat1[1:],ovs_stat2[1:]):
            item_list1=[e.strip() for e in items1.split("\n")]
            item_list2=[e.strip() for e in items2.split("\n")]
            portno=find_string('([0-9]+:)',item_list1[0])
            if not portno:
                continue
            rx_drop1=int(find_string('(?<=drop=)([0-9]+)',item_list1[0]))
            rx_drop2=int(find_string('(?<=drop=)([0-9]+)',item_list2[0]))
            rx_drop=rx_drop1-rx_drop2
            metrics.send("net.ovs.rx.drops",rx_drop,convport=convert_ovs_port(portno[:-1]),port=portno[:-1])
            print "net.ovs.rx.drops",rx_drop,convert_ovs_port(portno[:-1]),portno[:-1]
            
            rx_error1=int(find_string('(?<=errs=)([0-9]+)',item_list1[0]))
            rx_error2=int(find_string('(?<=errs=)([0-9]+)',item_list2[0]))
            rx_error=rx_error1-rx_error2
            metrics.send("net.ovs.rx.errors",rx_error,convport=convert_ovs_port(portno[:-1]),port=portno[:-1])
            print "net.ovs.rx.errors",rx_error,convert_ovs_port(portno[:-1]),portno[:-1]
            
            tx_drop1=int(find_string('(?<=drop=)([0-9]+)',item_list1[1]))
            tx_drop2=int(find_string('(?<=drop=)([0-9]+)',item_list2[1]))
            tx_drop=tx_drop1-tx_drop2
            metrics.send("net.ovs.tx.drops",tx_drop,convport=convert_ovs_port(portno[:-1]),port=portno[:-1])
            print "net.ovs.tx.drops",tx_drop,convert_ovs_port(portno[:-1]),portno[:-1]
            
            tx_error1=int(find_string('(?<=errs=)([0-9]+)',item_list1[1]))
            tx_error2=int(find_string('(?<=errs=)([0-9]+)',item_list2[1]))
            tx_error=tx_error1-tx_error2
            metrics.send("net.ovs.tx.errors",tx_error,convport=convert_ovs_port(portno[:-1]),port=portno[:-1])
            print "net.ovs.tx.errors",tx_error,convert_ovs_port(portno[:-1]),portno[:-1]
            
            rx_bytes1=float(find_string('(?<=bytes=)([0-9]+)',item_list1[0]))
            rx_bytes2=float(find_string('(?<=bytes=)([0-9]+)',item_list2[0]))
            rx_bytes=convert_bytes(rx_bytes1-rx_bytes2)
            metrics.send("net.ovs.rx.bytes",rx_bytes,convport=convert_ovs_port(portno[:-1]),port=portno[:-1])
            print "net.ovs.rx.bytes",rx_bytes,convert_ovs_port(portno[:-1]),portno[:-1]
            
            tx_bytes1=float(find_string('(?<=bytes=)([0-9]+)',item_list1[1]))
            tx_bytes2=float(find_string('(?<=bytes=)([0-9]+)',item_list2[1]))
            tx_bytes=convert_bytes(tx_bytes1-tx_bytes2)
            metrics.send("net.ovs.tx.bytes",tx_bytes,convport=convert_ovs_port(portno[:-1]),port=portno[:-1])
            print "net.ovs.tx.bytes",tx_bytes,convert_ovs_port(portno[:-1]),portno[:-1]
            
        metrics.send("net.ovs.cpu",float(ovs_cpu_mem_list[8]))
        print metric8,ovs_cpu_mem_list[8]
        metrics.send("net.ovs.mem",float(ovs_cpu_mem_list[9]))
        print metric9,ovs_cpu_mem_list[9]
        metrics.wait()

    except Exception, e1:
        print "Exception: ",e1
        continue

