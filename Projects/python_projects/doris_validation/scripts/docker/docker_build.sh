#!/bin/bash

set -eo pipefail

usage() {
  echo "
  Usage: $0
    arm_ctyunos:
      sh $0 --branch=2.0.x-rc04-ctyun --from_image=harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0:v1.0.0 --output_image=harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0-doris:v1.0.0 --arch=arm --image_os=ctyunos --dockerfile=Dockerfile --download_thirdparty=false --build_image=true --push_image=false
    x86_ctyunos:
      sh $0 --branch=2.0.x-rc04-ctyun --from_image=harbor.ctyuncdn.cn/bdyun/basic-image/amd-ctyunos:v1.0.0 --output_image=harbor.ctyuncdn.cn/bdyun/basic-image/amd-ctyunos-doris:v1.0.0 --arch=x86 --image_os=ctyunos --dockerfile=Dockerfile --download_thirdparty=false --build_image=true --push_image=false
    x86_centos:
      sh $0 --branch=2.0.x-rc04-ctyun --from_image=centos:7 --output_image=harbor.ctyuncdn.cn/bdyun/basic-image/amd-centos-doris:v1.0.0 --arch=x86 --image_os=centos --dockerfile=Dockerfile_centos --download_thirdparty=false --build_image=true --push_image=false
    "
}

OPTS=$(getopt \
  -o h \
  --long branch::,from_image::,output_image::,arch::,image_os::,dockerfile::,download_thirdparty::,build_image::,push_image::,help \
  -n "$0" \
  -- "$@")

if [ $? != 0 ]; then
  usage
  exit 1
fi

eval set -- "${OPTS}"

branch=''
from_image=''
output_image=''
arch=''
image_os=''
dockerfile=''
download_thirdparty='false'
build_image='false'
push_image='false'
help=0
while true; do
  case "$1" in
  --branch)
    branch=$2
    shift 2
    ;;
  --from_image)
    from_image=$2
    shift 2
    ;;
  --output_image)
    output_image=$2
    shift 2
    ;;
  --arch)
    arch=$2
    shift 2
    ;;
  --image_os)
    image_os=$2
    shift 2
    ;;
  --dockerfile)
    dockerfile=$2
    shift 2
    ;;
  --download_thirdparty)
    download_thirdparty=$2
    shift 2
    ;;
  --build_image)
    build_image=$2
    shift 2
    ;;
  --push_image)
    push_image=$2
    shift 2
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

if [ "$branch" = "" ]; then
  echo "error: branch is not given"
  exit 1
fi

if [ "$from_image" = "" ]; then
  echo "error: from_image is not given"
  exit 1
fi

if [ "$output_image" = "" ]; then
  echo "error: output_image is not given"
  exit 1
fi

if [ "$arch" = "" ]; then
  echo "error: arch is not given"
  exit 1
fi

if [ "$image_os" = "" ]; then
  echo "error: image_os is not given"
  exit 1
fi

if [ "$dockerfile" = "" ]; then
  dockerfile="Dockerfile"
fi

current_dir=$(
  cd $(dirname $0)
  pwd
)

dir_home=${current_dir}/${arch}

cleanup() {
  echo "cleanup..."
  src_softlink_path="${dir_home}/doris/thirdparty/src"
  if [ -L "${src_softlink_path}" ]; then
    echo "remove src softlink: ${src_softlink_path}"
    rm ${src_softlink_path}
  fi
}

trap cleanup SIGINT ERR EXIT

show_git_log_and_branch() {
  echo "===============================show_git_log_and_branch start========================================"
  echo "--------------------------------branch info--------------------------------"
  git branch
  echo "--------------------------------log info--------------------------------"
  git log -3
  echo "===============================show_git_log_and_branch end========================================"
}

update_doris() {
  echo "===============================update_doris========================================"
  cd ${dir_home}/doris
  git fetch origin ${branch}
  git checkout ${branch}
  git pull origin ${branch}
  show_git_log_and_branch
}

download_doris_thirdparty() {
  echo "===============================download_doris_thirdparty========================================"
  if [[ "$download_thirdparty" != "true" ]]; then
    echo "not download thirdparty"
    return
  fi

  cd ${dir_home}/doris/thirdparty
  thirdparty_src_path=$(realpath "../../thirdparty_src")
  if [[ ! -d ${thirdparty_src_path} ]]; then
    echo "thirdparty_src not exist: ${thirdparty_src_path}"
    exit 1
  fi

  ln -sf ${thirdparty_src_path} src
  echo "------------------------------download-thirdparty----------------------------------------"
  sh download-thirdparty.sh
  echo "------------------------------rm src/ dirs----------------------------------------"
  find src/ -mindepth 1 -maxdepth 1 -type d -exec rm -rf {} \;
  echo "------------------------------rm src softlink----------------------------------------"
  rm src
}

update_doris_validation() {
  echo "===============================update_doris_validation========================================"
  cd ${dir_home}/tools/doris_validation
  git fetch origin main
  git checkout main
  git pull origin main
  show_git_log_and_branch
}

docker_build() {
  echo "===============================docker_build========================================"
  if [[ "$build_image" != "true" ]]; then
    echo "not build image"
    return
  fi

  cd ${dir_home}
  echo "docker build cmd: docker build --build-arg from_image=${from_image} --build-arg arch=${arch} --build-arg image_os=${image_os} -t ${output_image} -f ${dockerfile} ."
  docker build --build-arg from_image=${from_image} --build-arg arch=${arch} --build-arg image_os=${image_os} -t ${output_image} -f ${dockerfile} .
}

docker_push() {
  echo "===============================docker_push========================================"
  if [[ "$push_image" != "true" ]]; then
    echo "not push image"
    return
  fi
  docker push ${output_image}
}

main() {
  cd ${dir_home}

  update_doris
  download_doris_thirdparty

  update_doris_validation

  docker_build
  docker_push
}

main