#!/bin/bash

# sh upgrade_doris.sh fe /data01/dingben/doris/doris_tars/1.2.5 doris-fe-1.2.5-1.0.0.tar.gz /usr/local doris-fe-1.2.3_ccdp3.3.3_1.0.0 dev_doris_fe
# sh upgrade_doris.sh be /data01/dingben/doris/doris_tars/1.2.5 doris-be-1.2.5-1.0.0.tar.gz /usr/local doris-be-1.2.3_ccdp3.3.3_1.0.0 dev_doris_be
# sh upgrade_doris.sh dependencies /data01/dingben/doris/doris_tars/1.2.5 doris-dependencies-1.2.5-1.0.0.tar.gz /usr/local doris-dependencies-1.2.3_ccdp3.3.3_1.0.0 dev_doris_be

component_type=$1
local_component_path=$2
component_name=$3
doris_installation_path=$4
doris_old_dirname=$5
ip_label=$6

ansible_copy() {
  echo "src=$local_component_path/$component_name dest=$doris_installation_path"
  ansible -i ip_list.conf $ip_label -m copy -a "src=$local_component_path/$component_name dest=$doris_installation_path"
}

operate_component_service() {
  local op_type=$1
  local component_type=$2
  ansible -i ip_list.conf $ip_label -m shell -a "systemctl $op_type doris-$component_type"
  ret=$?
  if [ $ret -ne 0 ]; then
    echo "fail to operate operate_component_service: $*"
    exit 1
  fi
}

upgrade_component() {
  ansible_copy
  ansible -i ip_list.conf $ip_label -m script -a "upgrade_component.sh $component_type $component_name $doris_installation_path $doris_old_dirname"
}

main() {
  if [ "$component_type" = "fe" ]; then
    operate_component_service stop fe
    upgrade_component
    operate_component_service start fe
  elif [ "$component_type" = "be" ]; then
    operate_component_service stop be
    upgrade_component
    operate_component_service start be
  elif [ "$component_type" = "dependencies" ]; then
    operate_component_service stop broker
    upgrade_component
    operate_component_service start broker
    #  elif [ "$component_type" = "all" ]; then
    #    upgrade_fe
    #    upgrade_be
    #    upgrade_dependencies
  else
    echo "component_type is incorrect"
  fi
}

main
