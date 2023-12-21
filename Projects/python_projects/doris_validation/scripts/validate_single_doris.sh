#!/bin/bash

if [[ -z "${JAVA_HOME}" ]]; then
  echo "error: JAVA_HOME is not set"
  exit 1
fi

usage() {
  echo "
  Optional options:
       --del_dirs   Delete dirs untared from tar.gz. Default false.

  Usage: $0
      JAVA_HOME和PATH的设置可选，如果系统已经有正确版本的java，则不需要设置这两个环境变量
      JAVA_HOME=/home/op/software/bisheng-jdk1.8.0_352 PATH=\$JAVA_HOME/bin:\$PATH sh $0 --tars_path=/xxx/doris_tars --local_ip=127.0.0.1
    "
}

OPTS=$(getopt \
  -o h \
  --long tars_path::,local_ip::,del_dirs,help \
  -n "$0" \
  -- "$@")

if [ $? != 0 ]; then
  usage
  exit 1
fi

eval set -- "${OPTS}"

tars_root_path=''
local_ip=''
del_dirs="false"
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
  --del_dirs)
    del_dirs="true"
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

current_dir=$(
  cd $(dirname $0)
  pwd
)

get_tar_file_name() {
  tar_category=$1
  tar_file_name=$(ls *-${tar_category}-*.tar.gz)
  echo $tar_file_name
}

untar_and_start() {
  tar_category=$1
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

  if [ "$tar_category" = "be" ]; then
    sudo sysctl -w vm.max_map_count=2000000
    ulimit -n 65536
  fi

  echo "starting: ./${dir_name}/bin/start_${tar_category}.sh --daemon"
  ./${dir_name}/bin/start_${tar_category}.sh --daemon
  check_ret_code $? "start ${tar_category}"
}

stop_server() {
  cd $tars_root_path
  tar_category=$1
  tar_file_name=$(get_tar_file_name $tar_category)
  if [ "$tar_file_name" = '' ]; then
    echo "error: there is no *-${tar_category}-*.tar.gz in $tars_root_path"
    exit 1
  fi

  dir_name=${tar_file_name%.tar.gz}
  echo "stopping: ./${dir_name}/bin/stop_${tar_category}.sh"
  ./${dir_name}/bin/stop_${tar_category}.sh
}

del_untar_dirs() {
  cd $tars_root_path
  if [[ $del_dirs = "true" ]]; then
    find . -maxdepth 1 -name "doris-*" -type d | xargs -t -I {} rm -rf {}
  fi
}

stop_server_and_clean() {
  stop_server fe
  stop_server be
  del_untar_dirs
}

check_ret_code() {
  ret_code=$1
  do_msg=$2
  if [ $ret_code -ne 0 ]; then
    echo "failed to $do_msg"
    stop_server_and_clean
    exit $ret_code
  fi
}

add_backends() {
  sleep 10s
  echo "adding backend ${local_ip}:9050"
  mysql -uroot -P9030 -h127.0.0.1 -e "ALTER SYSTEM ADD BACKEND \"${local_ip}:9050\";"
  check_ret_code $? "add backend"

  sleep 25s
  echo "show frontends and backends: "
  mysql -uroot -P9030 -h127.0.0.1 -e "show frontends\G; show backends\G;"
}

validate_doris() {
  python3_path=$(which python3)

  if [[ -f "${current_dir}/../validate_main.py" ]] && [[ "$python3_path" != "" ]]; then
    cd ${current_dir}/..
    python3 validate_main.py --ip="127.0.0.1" --user="root" --port=9030 --password="" --clean=false
    check_ret_code $? "validate_doris by cmd: python3 validate_main.py --ip=127.0.0.1 --user=root --port=9030 --password= --clean=false"
  else
    echo "there is no validate_main.py or python3, so validate doris running staus is not performed."
  fi
}

main() {
  cd $tars_root_path
  untar_and_start fe
  untar_and_start be
  add_backends
  validate_doris
  stop_server_and_clean
}

main
