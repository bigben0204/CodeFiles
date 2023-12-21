#!/bin/bash

# sh upgrade_component.sh fe doris-fe-1.2.5-1.0.0.tar.gz /usr/local doris-fe-1.2.3_ccdp3.3.3_1.0.0
# sh upgrade_component.sh be doris-be-1.2.5-1.0.0.tar.gz /usr/local doris-be-1.2.3_ccdp3.3.3_1.0.0
# sh upgrade_component.sh dependencies doris-dependencies-1.2.5-1.0.0.tar.gz /usr/local doris-dependencies-1.2.3_ccdp3.3.3_1.0.0

component_type=$1
tar_file_name=$2
doris_installation_path=$3
doris_old_dirname=$4

copy_config_files() {
  local_old_component_path=$1
  local_component_path=$2

  if [ "$component_type" = "fe" ]; then
    cp ${local_old_component_path}/conf/fe.conf ${local_component_path}/conf/
    cp ${local_old_component_path}/conf/fe_custom.conf ${local_component_path}/conf/
    cp ${local_old_component_path}/conf/hdfs-site.xml ${local_component_path}/conf/
    cp ${local_old_component_path}/conf/core-site.xml ${local_component_path}/conf/
  elif [ "$component_type" = "be" ]; then
    cp ${local_old_component_path}/conf/be.conf ${local_component_path}/conf/
    cp ${local_old_component_path}/conf/be_custom.conf ${local_component_path}/conf/
    cp ${local_old_component_path}/conf/hdfs-site.xml ${local_component_path}/conf/
    cp ${local_old_component_path}/conf/core-site.xml ${local_component_path}/conf/
  elif [ "$component_type" = "dependencies" ]; then
    cp ${local_old_component_path}/apache_hdfs_broker/conf/apache_hdfs_broker.conf ${local_component_path}/apache_hdfs_broker/conf/
    cp ${local_old_component_path}/apache_hdfs_broker/conf/log4j.properties ${local_component_path}/apache_hdfs_broker/conf/

    unzip -d ${local_component_path}/audit_loader ${local_component_path}/audit_loader/auditloader.zip
    cp ${local_old_component_path}/audit_loader/plugin.conf ${local_component_path}/audit_loader/

    rm ${local_component_path}/audit_loader/auditloader.zip
  else
    echo "unknown $component_type"
    exit 1
  fi
}

mv_new_component_path() {
  doris_installation_path=$1
  component_dir_name=$2
  tmp_local_component_path=$3
  component_type=$4

  time=`date "+%Y%m%d%H%M%S"`
  local_component_path=${doris_installation_path}/${component_dir_name}_${time}

  mv $tmp_local_component_path ${local_component_path}
  echo "$tmp_local_component_path is moved to ${local_component_path}"

  rm doris-$component_type
  ln -sf $local_component_path doris-$component_type
  chown -Rf doris:doris $local_component_path
  chown -Rf doris:doris doris-$component_type
}

main() {
  cd $doris_installation_path

  component_dir_name=${tar_file_name%.tar.gz}
  tmp_component_dir_name=${component_dir_name}_tmp
  mkdir -p $tmp_component_dir_name

  tar -xzf $tar_file_name -C $tmp_component_dir_name
  echo "$tar_file_name is extracted to $tmp_component_dir_name"

  tmp_local_component_path=${doris_installation_path}/${tmp_component_dir_name}/${component_dir_name}
  local_old_component_path=${doris_installation_path}/${doris_old_dirname}

  copy_config_files $local_old_component_path $tmp_local_component_path
  mv_new_component_path $doris_installation_path $component_dir_name $tmp_local_component_path $component_type

  rmdir $tmp_component_dir_name
}

main
