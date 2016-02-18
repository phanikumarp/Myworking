#!/usr/bin/python
import os
import potsdb
import datetime


### reading conf values
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
###### end of the conf reading ...####

current_path=os.getcwd()
now = datetime.datetime.now()
rttvalues=current_path +"/rtt_values.txt"
cmd = current_path+"/rtt.sh | sort -u -t, -k1,1 > rttvalues"
os.system(cmd)
file = open(rttvalues)
old_src= None 
old_dst= None
old_rtt=0
old_var=0
src_ser= None
des_ser= None
count=1
metric_rtt="net.rtt"
metric_rtt_var="net.rtt.variance"

metrics = potsdb.Client(tsdbhost,tsdbport,qsize=1000, host_tag=True, mps=100, check_host=True)
for line in file:
	#print line
	a = line.strip().split(" ")

	#if old_dst == a[2]:
	if src_ser == a[1]:
		old_src=a[0]
		old_dst=a[2] 
		old_rtt=float(a[4]) + float(old_rtt)
		old_var=float(a[5]) + float(old_var)
		count += 1

	else:
		#print count
		old_rtt=float(old_rtt) / count
		old_var=float(old_var) / count 
		if old_src != None:
		
			metrics.send(metric_rtt,old_rtt,src=old_src,srcservice=src_ser,dst=old_dst,desservice=des_ser,appname=appname)
			metrics.send(metric_rtt_var,old_var,src=old_src,srcservice=src_ser,dst=old_dst,desservice=des_ser,appname=appname)

			#print metric_rtt_var,old_var,old_src,old_dst
			print str(now),metric_rtt,old_rtt,old_src,src_ser,old_dst,des_ser,appname,"11111111111"  # push last record
			#print metric_rtt_var,old_var,old_src,src_ser,old_dst,des_ser
	
	old_src = a[0]
	src_ser = a[1]
	old_dst = a[2]
	des_ser = a[3]
	old_rtt = a[4]
	old_var = a[5]
	count = 1

print str(now),metric_rtt,old_rtt,old_src,src_ser,old_dst,des_ser,appname,"22222222221"  # push last record
#print metric_rtt_var,old_var,old_src,src_ser,old_dst,des_ser 
#metrics.send(metric_rtt, old_rtt,src=old_src,dst=old_dst)
#metrics.send(metric_rtt, old_var,src=old_src,dst=old_dst)
metrics.send(metric_rtt,old_rtt,src=old_src,srcservice=src_ser,dst=old_dst,desservice=des_ser,appname=appname)
metrics.send(metric_rtt_var,old_var,src=old_src,srcservice=src_ser,dst=old_dst,desservice=des_ser,appname=appname)
metrics.wait()
