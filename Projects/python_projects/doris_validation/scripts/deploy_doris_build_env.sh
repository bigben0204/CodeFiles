#!/bin/bash

usage() {
  echo "
  Prepare:
    apache-maven-3.6.3-bin.tar.gz
    bisheng-jdk-8u352-linux-x64.tar.gz
    doris_validation.zip
    jdbc/mysql-connector-java-8.0.28.jar jdbc/ojdbc8.jar jdbc/postgresql-42.5.1.jar
    mpp_doris_git.tar.gz
    node-v12.13.0-linux-x64.tar.gz
    repository.tar.gz
    thirdparty_src.tar.gz
    settings.xml
    ldb_toolchain_gen.sh
    deploy_doris_build_env.sh
    pipeline_build.sh

  Usage:
      sh $0 --work_root_dir=/data01/doris --installation_package_dir=/data01/prepare --compile_branch=2.0.1-rc04-ctyun --compile_flag=0 --doris_package_version=2.0.1-1.0.0
    "
}

OPTS=$(getopt \
  -o h \
  --long work_root_dir::,installation_package_dir::,compile_branch::,compile_flag::,doris_package_version::,help \
  -n "$0" \
  -- "$@")

if [ $? != 0 ]; then
  usage
  exit 1
fi

eval set -- "${OPTS}"

work_root_dir=''
installation_package_dir=''
compile_branch=''
compile_flag=''
doris_package_version=''
help=0
while true; do
  case "$1" in
  --work_root_dir)
    work_root_dir=$2
    shift 2
    ;;
  --installation_package_dir)
    installation_package_dir=$2
    shift 2
    ;;
  --compile_branch)
    compile_branch=$2
    shift 2
    ;;
  --compile_flag)
    compile_flag=$2
    shift 2
    ;;
  --doris_package_version)
    doris_package_version=$2
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

current_dir=$(
  cd $(dirname $0)
  pwd
)

if [ "$work_root_dir" = "" ]; then
  echo "error: work_root_dir is not given"
  exit 1
fi

if [ "$compile_branch" = "" ]; then
  echo "error: compile_branch is not given"
  exit 1
fi

install_compile_tools() {
  echo "step: install_compile_tools"

  sudo yum install -y byacc patch automake libtool make which file ncurses-devel gettext-devel unzip bzip2 zip util-linux wget git
}

generate_ldb_tool_chain() {
  echo "step: generate_ldb_tool_chain"

  mkdir -p software
  if [ "$arch" = "arm" ]; then
    sh ${installation_package_dir}/ldb_toolchain_gen_aarch64.sh ${work_root_dir}/software/ldb_toolchain
  else
    sh ${installation_package_dir}/ldb_toolchain_gen.sh ${work_root_dir}/software/ldb_toolchain
  fi
}

untar_compile_tools() {
  echo "step: untar_compile_tools"

  mkdir -p software
  if [ "$arch" = "arm" ]; then
    tar -xzf ${installation_package_dir}/bisheng-jdk-8u352-linux-aarch64.tar.gz -C software
    tar -xf ${installation_package_dir}/node-v16.3.0-linux-arm64.tar.xz -C software
  else
    tar -xzf ${installation_package_dir}/bisheng-jdk-8u352-linux-x64.tar.gz -C software
    tar -xf ${installation_package_dir}/node-v12.13.0-linux-x64.tar.gz -C software
  fi
  tar -xf ${installation_package_dir}/apache-maven-3.6.3-bin.tar.gz -C software
  cp ${installation_package_dir}/settings.xml software/apache-maven-3.6.3/conf/
}

untar_mvn_jars() {
  echo "step: untar_mvn_jars"

  mkdir -p software/.m2
  tar -xzf ${installation_package_dir}/repository.tar.gz -C software/.m2/
  sed -i "55i <localRepository>${work_root_dir}/software/.m2/repository</localRepository>" software/apache-maven-3.6.3/conf/settings.xml
}

untar_validation_tools() {
  echo "step: untar_validation_tools"

  unzip -d software ${installation_package_dir}/doris_validation.zip
}

cp_jdbc_jars() {
  echo "step: cp_jdbc_jars"

  cp -rdp ${installation_package_dir}/jdbc software
}

init_architecture() {
  echo "step: init_architecture"

  arch=$(uname -a | grep aarch64)
  if [ "$arch" != "" ]; then
    arch="arm"
  else
    arch="x86"
  fi
  echo "arch is: $arch"
}

untar_doris() {
  echo "step: untar_doris"

  tar -xzf ${installation_package_dir}/mpp_doris_git.tar.gz -C .
  cd doris
  git checkout $compile_branch

  cd $work_root_dir
  src_name="thirdparty_src"
  tar -xzf ${installation_package_dir}/${src_name}.tar.gz -C doris/thirdparty/
  rm -rf doris/thirdparty/src
  mv doris/thirdparty/${src_name} doris/thirdparty/src
}

gen_custom_env_sh() {
  echo "step: gen_custom_env_sh"

  mkdir custom_env_files
  if [ "$arch" = "arm" ]; then
    cat >custom_env_files/custom_env.sh <<EOF
#!/bin/bash

export JAVA_HOME=${work_root_dir}/software/bisheng-jdk1.8.0_352
export PATH=\$JAVA_HOME/bin:\$PATH
export PATH=${work_root_dir}/software/apache-maven-3.6.3/bin:\$PATH
export PATH=${work_root_dir}/software/node-v16.3.0-linux-arm64/bin:\$PATH
export PATH=${work_root_dir}/software/ldb_toolchain/bin:\$PATH
export DORIS_THIRDPARTY=${work_root_dir}/software/thirdparty
EOF
  else
    cat >custom_env_files/custom_env.sh <<EOF
#!/bin/bash

export JAVA_HOME=${work_root_dir}/software/bisheng-jdk1.8.0_352
export PATH=\$JAVA_HOME/bin:\$PATH
export PATH=${work_root_dir}/software/apache-maven-3.6.3/bin:\$PATH
export PATH=${work_root_dir}/software/node-v12.13.0-linux-x64/bin:\$PATH
export PATH=${work_root_dir}/software/ldb_toolchain/bin:\$PATH
export DORIS_THIRDPARTY=${work_root_dir}/software/thirdparty
EOF
  fi
}

compile_doris() {
  echo "step: compile_doris"

  cd ${work_root_dir}/doris
  #编译全部组件 #编译be中的meta-tool
  sh build.sh > build.log 2>&1 && sh build.sh --be --meta-tool > build_meta_tool.log 2>&1
  ret_code=$?
  if [ $ret_code -ne 0 ]; then
    echo "failed to compile_doris"
    exit $ret_code
  fi

  cp -rdp ${installation_package_dir}/jdbc ${work_root_dir}/doris/output
}

package_and_validate() {
  echo "step: package_and_validate"

  cd $work_root_dir
  mkdir -p ${work_root_dir}/doris_tars

  cd software/doris_validation/scripts
  sh package_doris_output.sh "${work_root_dir}/doris/output" "${work_root_dir}/doris_tars" "${doris_package_version}" "doris"

  sudo sysctl -w vm.max_map_count=2000000
  sh validate_single_doris.sh "${work_root_dir}/doris_tars" 127.0.0.1
}

get_version() {
  full_branch_name=${compile_branch#*/}
  main_version=$(echo $full_branch_name | awk -F '-' '{print $1}')
  echo $main_version-1.0.0
}

get_compile_evn() {
  if [ "$arch" = "arm" ]; then
    echo "USE_UNWIND=OFF USE_AVX2=0 ENABLE_PCH=OFF"
  else
    echo ""
  fi
}

print_compile_cmd() {
  echo "step: print_compile_cmd"
  version=$(get_version)
  compile_env=$(get_compile_evn)

  cat > $work_root_dir/doris_compile_cmd.sh <<EOF
source ${work_root_dir}/custom_env_files/custom_env.sh
cd ${work_root_dir}/doris
sh thirdparty/build-thirdparty.sh > thirdparty/build-thirdparty.log 2>&1
mkdir -p ${work_root_dir}/software/thirdparty; mv thirdparty/installed ${work_root_dir}/software/thirdparty
DORIS_GIT_DIR=${work_root_dir}/doris DORIS_SOFTWARE_DIR=${work_root_dir} CUSTOM_ENV_SH_NAME=custom_env.sh VERSION=${version} CLEAN_LAST_BUILD=false COMPILE=true PACKAGE=true VALIDATE=true UPLOAD_TO_REPO=false COMPILE_ENV="${compile_env}" REPO_ROOT_DIR_NAME=TODO sh pipeline_build.sh
EOF

  echo "==================================doris compile cmd end========================================="
  cat $work_root_dir/doris_compile_cmd.sh
  echo "==================================doris compile cmd end========================================="
  echo "===========doris compile cmd file path: $work_root_dir/doris_compile_cmd.sh"
}

main() {
  cd $work_root_dir
  init_architecture
  install_compile_tools
  generate_ldb_tool_chain
  untar_compile_tools
  untar_mvn_jars
  untar_validation_tools
  cp_jdbc_jars
  untar_doris
  gen_custom_env_sh
  print_compile_cmd

  if [ "$compile_flag" = "1" ]; then
    compile_doris
    package_and_validate
  else
    echo "not compile"
  fi

  echo "deploy_doris_build_env successfully"
}

main
