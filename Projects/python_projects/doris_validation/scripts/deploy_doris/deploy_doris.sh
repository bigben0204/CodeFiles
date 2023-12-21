#!/bin/bash

usage() {
  echo "
  Optional options:
       --is_start   start the server. Default OFF.

  Usage: $0
      sh $0 --tars_path=/xxx_fe_tars_dir --local_ip=xxx.xxx.xxx.xxx --tar_category=fe --storage_path=\"/xxx/doris-meta\"
      sh $0 --tars_path=/xxx_be_tars_dir --local_ip=xxx.xxx.xxx.xxx --tar_category=be --storage_path=\"/xx1/doris-storage;/xxx/doris-storage\"
    "
}

OPTS=$(getopt \
  -o h \
  --long tars_path::,local_ip::,tar_category::,storage_path::,is_start,help \
  -n "$0" \
  -- "$@")

if [ $? != 0 ]; then
  usage
  exit 1
fi

eval set -- "${OPTS}"

tars_root_path=''
local_ip=''
tar_category=''
storage_path=''
is_start=0
help=0
while true; do
  case "$1" in
  --tars_path)
    tars_root_path=$2
    shift 2
    ;;
  --local_ip)
    local_ip=$2
    shift 2
    ;;
  --tar_category)
    tar_category=$2
    shift 2
    ;;
  --storage_path)
    storage_path=$2
    shift 2
    ;;
  --is_start)
    is_start=1
    shift
    ;;
  -h | --help)
    help=1
    shift
    ;;
  --)
    shift
    break
    ;;
  *)
    echo "Internal error"
    exit 1
    ;;
  esac
done

if [[ "${help}" -eq 1 ]]; then
  usage
  exit
fi

if [ "$storage_path" = "" ]; then
  echo "error: fe/be storage_path is not given"
  exit 1
fi

get_tar_file_name() {
  tar_category=$1
  tar_file_name=$(ls *-${tar_category}-*.tar.gz)
  echo $tar_file_name
}

untar_and_start() {
  tar_file_name=$(get_tar_file_name $tar_category)
  if [ "$tar_file_name" = '' ]; then
    echo "error: there is no *-${tar_category}-*.tar.gz in $tars_root_path"
    exit 1
  fi

  dir_name=${tar_file_name%.tar.gz}

  if [ -d "$dir_name" ]; then
    ./${dir_name}/bin/stop_${tar_category}.sh
    rm -r $dir_name
  fi

  echo "untar $tar_file_name to $tars_root_path"
  tar -xzf $tar_file_name
  priority_networks=${local_ip%.*}
  echo "priority_networks = ${priority_networks}.0/24" >>${dir_name}/conf/${tar_category}.conf
  if [ "$tar_category" = 'fe' ]; then
    echo "meta_dir=$storage_path" >>${dir_name}/conf/${tar_category}.conf
  elif [ "$tar_category" = 'be' ]; then
    echo "storage_root_path=$storage_path" >>${dir_name}/conf/${tar_category}.conf
    sudo sysctl -w vm.max_map_count=2000000
  fi

  if [[ "${is_start}" -eq 1 ]]; then
    echo "starting: ./${dir_name}/bin/start_${tar_category}.sh --daemon"
    ./${dir_name}/bin/start_${tar_category}.sh --daemon
  fi
}

main() {
  cd $tars_root_path
  untar_and_start
}

main
