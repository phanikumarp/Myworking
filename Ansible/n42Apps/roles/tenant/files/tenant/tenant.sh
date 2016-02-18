#!/bin/bash
base_path="`pwd`/tenant"
#base_path=`pwd`
while true;do
    . /root/keystonerc_admin
    > $base_path/dump.txt
    > $base_path/final.txt
    > $base_path/vm_details.txt
    > $base_path/tenant_details.txt
    ovs-ofctl show br-int | grep "qvo\|tap" | awk -F"(" '{print $1}' > $base_path/ofport-dump.txt
    python $base_path/convert.py > $base_path/ofport.txt
    neutron net-list | grep -v "rally" | awk ' {print $2}' | awk "NR>3" | sed '$d' > $base_path/net_list.txt
    keystone tenant-list | grep -v ctx_rally* | tail -n +4 | head -n -1 > $base_path/tenant_details.txt #Tenant ID, Tenant Name

    while read net_id; do
        net_type=`neutron net-show $net_id | sed -n '8p' | awk '{print $4}'`
        net_type=${net_type^^}
        if [ "$net_type" == "EXTERNAL" ] || [ "$net_id" == "|" ] || [ "$net_id" == "|" ]
        then
            continue
        fi
        var=`neutron net-show $net_id | sed -n '9p;5p;6p' | awk '{print $4}'`
        seg_id=`echo $var | cut --d=" " -f3`
        net_name=`echo $var | cut --d=" " -f2`
        ten_id=`neutron net-show $net_id | grep tenant_id | cut --d="|" -f3`
        echo "$net_name $seg_id $net_id$ten_id"
        echo "$net_name $seg_id $net_id$ten_id" >> $base_path/dump.txt
    done < $base_path/net_list.txt
    vswitch=`hostname`

    while read ofport; do
        new_port=`echo $ofport | cut -d' ' -f1`
        ofport=`echo $ofport | cut -d' ' -f2`
        instance_port=`ovs-ofctl show br-int | grep "tap\|qvo" | awk '{print $1}' | grep "^$ofport(tap\|^$ofport(qvo" | cut -d'(' -f2 | cut -d')' -f1`
        instance_tag=`ovs-vsctl list port | grep -A 5 $instance_port | grep tag | cut -d':' -f2 | cut -d' ' -f2`
        tunnel_id_hex=`ovs-ofctl dump-flows br-tun | grep -w dl_vlan=$instance_tag | cut -d':' -f2 | cut -d',' -f1 | cut -c 3-`
        if [ -z $tunnel_id_hex ]
        then
            continue;
        fi
        tunnel_id=`echo $((16#$tunnel_id_hex))`
        network_id=`cat $base_path/dump.txt  | awk -v var="$tunnel_id" -F' ' '$2==var' | cut -d' ' -f3`
        tenant_id=`cat $base_path/dump.txt  | awk -v var="$tunnel_id" -F' ' '$2==var' | cut -d' ' -f4`
        tenant_name=`awk -F " " -v var="$tenant_id" '$2 == var' $base_path/tenant_details.txt | cut --d="|" -f3`
        echo "$new_port $network_id $tenant_id $vswitch $tenant_name"
        echo "${new_port//[[:blank:]]/} ${network_id//[[:blank:]]/} ${tenant_id//[[:blank:]]/} ${vswitch//[[:blank:]]/} ${tenant_name//[[:blank:]]/}" >> $base_path/final.txt
    done < $base_path/ofport.txt

    nova list --all-tenants | tail -n +4 | head -n -1 | grep "Running" | cut --d=" " -f2 | while read vm_id; do
        nova show $vm_id > $base_path/temp.txt
        tena_id=`cat $base_path/temp.txt | grep "tenant_id" | cut --d="|" -f3`
        vm_name=`cat $base_path/temp.txt | grep -w "name" | cut --d="|" -f3`
        ips=`cat $base_path/temp.txt | grep "network" | cut --d="|" -f3 `
        if [[ $ips =~ [,]+  ]]
        then
            vm_ip=`cat $base_path/temp.txt | grep "network" | cut --d="|" -f3 | cut --d="," -f1`
            float_ip=`cat $base_path/temp.txt | grep "network" | cut --d="|" -f3 | cut --d="," -f2`
        else
            vm_ip=`cat $base_path/temp.txt | grep "network" | cut --d="|" -f3 | cut --d="," -f1`
            float_ip="N/A"
        fi
        host_name=`cat $base_path/temp.txt | grep "hypervisor_hostname" | cut --d="|" -f3`
        te_name=`cat $base_path/tenant_details.txt | grep $tena_id | cut --d="|" -f3`
        net_name=`cat $base_path/temp.txt | grep "network" | cut --d=" " -f2` 
        echo "${tena_id//[[:blank:]]/}|${vm_name//[[:blank:]]/}|${vm_ip//[[:blank:]]/}|${float_ip//[[:blank:]]/}|${te_name//[[:blank:]]/}|${host_name//[[:blank:]]/}|$vm_id|${net_name//[[:blank:]]/}"
        echo "${tena_id//[[:blank:]]/}|${vm_name//[[:blank:]]/}|${vm_ip//[[:blank:]]/}|${float_ip//[[:blank:]]/}|${te_name//[[:blank:]]/}|${host_name//[[:blank:]]/}|$vm_id|${net_name//[[:blank:]]/}" >> $base_path/vm_details.txt
    done

    while read id; do
        t_id=`echo $id | cut --d=" " -f2`
        t_name=`echo $id | cut --d="|" -f3`
        check=`cat $base_path/vm_details.txt | grep $t_id`
        if [ -z "$check" ]
        then
            echo "$t_id|NO_VM|NO_IP|NO_IP|$t_name|NO_HOST|NO_VMID|NULL_NET"
            echo "$t_id|NO_VM|NO_IP|NO_IP|$t_name|NO_HOST|NO_VMID|NULL_NET" >> $base_path/vm_details.txt
        else
            continue
        fi
    done < $base_path/tenant_details.txt
    neutron router-list | grep ip_address | rev | cut -d"|" -f4 | rev | rev | cut -d'"' -f2 | rev > $base_path/gateway_list.txt

    topTalkerIp=175.126.103.49
    username=root
    password=son123!
    
    #skip strict host key verification check using following command.
    #sshpass -p '<your_password>' ssh <your_server_ip> -o StrictHostKeyChecking=no
    
    sudo sshpass -p "$password" scp $base_path/final.txt $username@$topTalkerIp:/root/netflow/dumps/top_talker_input/$vswitch.txt || echo "Error While Copying(final.txt) to $topTalkerIp"
    #cat $base_path/vm_details.txt | sshpass -p $password ssh $username@$topTalkerIp 'cat > /root/netflow/dumps/vm_details.txt' || echo "Error While Copying(vm_details.txt) to $topTalkerIp"
    #cat $base_path/gateway_list.txt | sshpass -p $password ssh $username@$topTalkerIp 'cat > /root/netflow/dumps/gateway_list.txt' || echo "Error While Copying(gateway_list.txt) to $topTalkerIp"
    echo "Data Copied"
    sleep 5
done
