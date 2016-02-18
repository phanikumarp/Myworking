'''
Author: Veerendra.K
Desrciption:
When this parameter is set to true(In Netflow configuration), "the 7 most significant bits of In/Out interface number 
is replaced with the 7 least significant bits of Engine ID. This will help interface number 
collision happen less likely"-This Python script file convert the OVS port
'''
import os
import subprocess
base_path=os.path.dirname(os.path.realpath(__file__))
f1=open('{}/ofport-dump.txt'.format(base_path),'r')
engine_id = subprocess.check_output('sudo ovs-vsctl list netflow | grep engine_id | cut -d":" -f2', shell=True)
bin_engine_id=bin(int(engine_id))[2:].zfill(8)[1:]
line=f1.readlines()
for ovs_port in line:
	bin_port=bin(int(ovs_port))[2:].zfill(16)
	new_port=bin_engine_id+bin_port[7:]
	print ("{}{}".format(int(new_port,2),ovs_port).rstrip('\n'))

