#!/bin/bash

usage() {
  echo "
  Optional options:
       --delete_dir   delete all dirs. Default false.
       --tar_category fe or be

  Usage: $0
      sh $0 --storage_path=\"/xxx/doris-meta\" --user_group=axeadmin:axeadmin --doris_tars_path=/data01/doris/doris_tars --tar_category=fe
      sh $0 --storage_path=\"/xx1/doris-storage;/xxx/doris-storage\" --user_group=axeadmin:axeadmin --doris_tars_path=/data01/doris/doris_tars --tar_category=be
    "
}

OPTS=$(getopt \
  -o h \
  --long storage_path::,user_group::,doris_tars_path::,tar_category::,delete_dir,help \
  -n "$0" \
  -- "$@")

if [ $? != 0 ]; then
  usage
  exit 1
fi

eval set -- "${OPTS}"

storage_path=''
user_group=''
doris_tars_path=''
tar_category=''
data_root=''
delete_dir=0
help=0
while true; do
  case "$1" in
  --storage_path)
    storage_path=$2
    shift 2
    ;;
  --user_group)
    user_group=$2
    shift 2
    ;;
  --doris_tars_path)
    doris_tars_path=$2
    shift 2
    ;;
  --tar_category)
    tar_category=$2
    shift 2
    ;;
  --delete_dir)
    delete_dir=1
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

mkdir_and_chown() {
  dir_path=$1
  user_group=$2
  sudo mkdir -p $dir_path
  sudo chown -Rf $user_group $dir_path
  echo "sudo mkdir -p $dir_path && sudo chown -Rf $user_group $dir_path"
}

chown_and_delete_dir() {
  dir_path=$1
  user_group=$2
  sudo chown -Rf $user_group $dir_path
  sudo rm -rf $dir_path
  echo "sudo chown -Rf $user_group $dir_path && rm -rf $dir_path"
}

mkdir_op() {
  mkdir_and_chown $doris_tars_path $user_group

  if [ "$tar_category" = 'fe' ]; then
    # 修改fe存储目录权限
    mkdir_and_chown ${storage_path} $user_group
  elif [ "$tar_category" = 'be' ]; then
    paths=(${storage_path//;/ })
    for path in "${paths[@]}"; do
      # 修改be存储目录权限
      mkdir_and_chown ${path} $user_group
    done
  fi
}

deldir_op() {
  if [ "$tar_category" = 'fe' ]; then
    chown_and_delete_dir ${storage_path} $user_group
  elif [ "$tar_category" = 'be' ]; then
    paths=(${storage_path//;/ })
    for path in "${paths[@]}"; do
      chown_and_delete_dir /${dir_name}/${storage_path} $user_group
    done
  fi
}

main() {
  umask 0022

  if [[ ${delete_dir} -eq 0 ]]; then
    mkdir_op
  else
    deldir_op
  fi
}

main
