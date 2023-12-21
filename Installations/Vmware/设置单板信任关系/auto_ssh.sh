#!/bin/bash
cat password.txt | while read LINE
do
    ip=`echo $LINE | awk -F ' ' '{print $1}'`
    password=`echo $LINE | awk -F ' ' '{print $2}'`
    
    echo $ip
    echo $password

    ssh-keygen -R $ip -f ~/.ssh/known_hosts
    
    expect -c"
        spawn ssh-copy-id -f -i ~/.ssh/id_rsa.pub root@$ip
        expect {
            \"*yes/no*\" {send \"yes\r\"; exp_continue}
            \"*assword*\" {send -- \"$password\n\"; exp_contine}
        }
    "
done
