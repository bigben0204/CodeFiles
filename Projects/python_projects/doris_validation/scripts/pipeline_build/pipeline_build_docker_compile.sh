#!/bin/bash

umask 022

check_and_set_vars() {
  if [[ -z "${DORIS_GIT_DIR}" ]] || [[ ! -d "${DORIS_GIT_DIR}" ]]; then
    echo "error: DORIS_GIT_DIR is not set or does not exist, value: $DORIS_GIT_DIR"
    exit 1
  fi

  if [[ -z "${MVN_REPOSITORY}" ]] || [[ ! -d "${MVN_REPOSITORY}" ]]; then
    echo "error: MVN_REPOSITORY is not set or does not exist, value: $MVN_REPOSITORY"
    exit 1
  fi

  if [[ -z "${VERSION}" ]] || [[ "${VERSION}" = "2.0.x-1.0.0" ]]; then
    echo "error: VERSION is not set or VERSION is invalid"
    exit 1
  fi

  if [[ -z "${CLEAN_LAST_BUILD}" ]]; then
    echo "info: CLEAN_LAST_BUILD is not set and default false is used"
    CLEAN_LAST_BUILD="false"
  fi

  if [[ -z "${UPLOAD_TO_REPO}" ]]; then
    echo "info: UPLOAD_TO_REPO is not set and default false is used"
    UPLOAD_TO_REPO="false"
  elif [[ "${UPLOAD_TO_REPO}" = "true" ]] && [[ -z "${REPO_ROOT_DIR_NAME}" ]]; then
    echo "error: UPLOAD_TO_REPO is true, but REPO_ROOT_DIR_NAME is not set"
    exit 1
  fi

  workspace_dir=$(realpath ${DORIS_GIT_DIR}/..)
  doris_tars_dir=$workspace_dir/doris_tars
}

print_info() {
  echo "========================step: print_info============================"
  echo "VERSION=$VERSION"
  echo "DORIS_GIT_DIR=$DORIS_GIT_DIR"
  echo "CLEAN_LAST_BUILD=$CLEAN_LAST_BUILD"
  echo "UPLOAD_TO_REPO=$UPLOAD_TO_REPO"
  echo "REPO_ROOT_DIR_NAME=$REPO_ROOT_DIR_NAME"
  echo "DOCKER_IMAGE_NAME=$DOCKER_IMAGE_NAME"
  echo "COMPILE_ENV=$COMPILE_ENV"
  echo "MVN_REPOSITORY=$MVN_REPOSITORY"
  echo "GIT_COMMIT=$GIT_COMMIT"
  echo "GIT_BRANCH=$GIT_BRANCH"

  echo "debug env: VERSION=$VERSION DORIS_GIT_DIR=$DORIS_GIT_DIR CLEAN_LAST_BUILD=$CLEAN_LAST_BUILD UPLOAD_TO_REPO=$UPLOAD_TO_REPO REPO_ROOT_DIR_NAME=$REPO_ROOT_DIR_NAME DOCKER_IMAGE_NAME=$DOCKER_IMAGE_NAME COMPILE_ENV=\"$COMPILE_ENV\" MVN_REPOSITORY=$MVN_REPOSITORY"
}

check_ret_code() {
  ret_code=$1
  do_msg=$2
  if [ $ret_code -ne 0 ]; then
    echo "failed to $do_msg"
    exit $ret_code
  fi
}

modify_submodule_url() {
  echo "========================step: modify_submodule_url============================"
  cd $DORIS_GIT_DIR

  #默认编译meta_tool，不用再编译两次
  sed -i "s/BUILD_META_TOOL='OFF'/BUILD_META_TOOL='ON'/g" build.sh
  #修改子模块路径到gitee
  sed -i 's/https:\/\/github.com\/apache\/doris-thirdparty.git/https:\/\/gitee.com\/mirrors_apache\/doris-thirdparty.git/g' .gitmodules
  sed -i 's/https:\/\/github.com\/apache\/doris-thirdparty.git/https:\/\/gitee.com\/mirrors_apache\/doris-thirdparty.git/g' .git/config
}

modify_src_files() {
  echo "========================step: modify_src_files============================"
  cd $DORIS_GIT_DIR

  sed -i "s/static inline void a_barrier()/static inline void a_barrier(void)/g" be/src/glibc-compatibility/musl/aarch64/atomic_arch.h
  sed -i "s/static inline void a_crash()/static inline void a_crash(void)/g" be/src/glibc-compatibility/musl/atomic.h
}

build_doris() {
  echo "========================step: build_doris============================"
  if [[ "$COMPILE" = "true" ]]; then
    cd $DORIS_GIT_DIR
    rm -rf output

    if [[ "$CLEAN_LAST_BUILD" = "true" ]]; then
      echo "build cmd: docker run --privileged --rm -v $PWD:$PWD -v $MVN_REPOSITORY:/root/.m2/repository ${DOCKER_IMAGE_NAME} /bin/bash -c \"cd $PWD && $COMPILE_ENV sh build.sh --clean && $COMPILE_ENV sh build.sh\""
      docker run --privileged --rm -v $PWD:$PWD -v $MVN_REPOSITORY:/root/.m2/repository ${DOCKER_IMAGE_NAME} /bin/bash -c "cd $PWD && $COMPILE_ENV sh build.sh --clean && $COMPILE_ENV sh build.sh"
    else
      # 不同环境需要修改特定的构建参数，COMPILE_ENV在各环境有差异
      echo "build cmd: docker run --privileged --rm -v $PWD:$PWD -v $MVN_REPOSITORY:/root/.m2/repository ${DOCKER_IMAGE_NAME} /bin/bash -c \"cd $PWD && $COMPILE_ENV sh build.sh\""
      docker run --privileged --rm -v $PWD:$PWD -v $MVN_REPOSITORY:/root/.m2/repository ${DOCKER_IMAGE_NAME} /bin/bash -c "cd $PWD && $COMPILE_ENV sh build.sh"
    fi
    check_ret_code $? "build doris" 1
  else
    echo "not to build"
  fi
}

get_version() {
  echo $VERSION
}

package_tar() {
  echo "------------------package tar begin---------------------------"
  version=$(get_version)
  echo "package cmd: docker run --privileged --rm -v $DORIS_GIT_DIR:$DORIS_GIT_DIR -v $doris_tars_dir:$doris_tars_dir ${DOCKER_IMAGE_NAME} /bin/bash -c \"rm -rf $DORIS_GIT_DIR/output/jdbc && cp -rdp /var/local/jdbc $DORIS_GIT_DIR/output/ && sh /var/local/doris_validation/scripts/package_doris_output.sh $DORIS_GIT_DIR/output $doris_tars_dir/$version $version doris\""
  docker run --privileged --rm -v $DORIS_GIT_DIR:$DORIS_GIT_DIR -v $doris_tars_dir:$doris_tars_dir ${DOCKER_IMAGE_NAME} /bin/bash -c "rm -rf $DORIS_GIT_DIR/output/jdbc && cp -rdp /var/local/jdbc $DORIS_GIT_DIR/output/ && sh /var/local/doris_validation/scripts/package_doris_output.sh $DORIS_GIT_DIR/output $doris_tars_dir/$version $version doris"
  check_ret_code $? "package tar"
  echo "------------------package tar end---------------------------"
}

list_tar() {
  version=$(get_version)
  ls -al $doris_tars_dir/$version/*.tar.gz
  md5sum $doris_tars_dir/$version/*.tar.gz
}

package_list_tar() {
  echo "========================step: package_list_tar============================"
  if [[ "$PACKAGE" = "true" ]]; then
    cd $DORIS_GIT_DIR

    if [[ ! -d "output" ]]; then
      check_ret_code 1 "find output directory"
    fi

    package_tar
  else
    echo "not to package tar"
  fi

  list_tar
}

validate_tar() {
  echo "========================step: validate_tar============================"
  if [[ "$VALIDATE" = "true" ]]; then
    cd $DORIS_GIT_DIR

    echo "------------------validate tar begin---------------------------"
    version=$(get_version)
    echo "validate cmd: docker run --privileged --rm -v $doris_tars_dir/$version:$doris_tars_dir/$version ${DOCKER_IMAGE_NAME} /bin/bash -c \"cd /var/local/doris_validation/scripts && sh validate_single_doris.sh --tars_path=$doris_tars_dir/$version --local_ip=127.0.0.1 --del_dirs\""
    docker run --privileged --rm -v $doris_tars_dir/$version:$doris_tars_dir/$version ${DOCKER_IMAGE_NAME} /bin/bash -c "cd /var/local/doris_validation/scripts && sh validate_single_doris.sh --tars_path=$doris_tars_dir/$version --local_ip=127.0.0.1 --del_dirs"
    check_ret_code $? "validate tar"
    echo "------------------validate tar end---------------------------"
  else
    echo "not to validate tar"
  fi
}

upload_to_repo() {
  echo "========================step: upload_to_repo============================"
  version=$(get_version)
  echo "
curl cmd:
cd $doris_tars_dir/$version
curl -u sys_deployer:xxx --upload-file doris-be-${version}.tar.gz https://devops.ctcdn.cn/nexus/repository/raw-repo/BigDataProduction/ccd-release-generic/${REPO_ROOT_DIR_NAME}/doris-be-${version}.tar.gz
curl -u sys_deployer:xxx --upload-file doris-fe-${version}.tar.gz https://devops.ctcdn.cn/nexus/repository/raw-repo/BigDataProduction/ccd-release-generic/${REPO_ROOT_DIR_NAME}/doris-fe-${version}.tar.gz
curl -u sys_deployer:xxx --upload-file doris-dependencies-${version}.tar.gz https://devops.ctcdn.cn/nexus/repository/raw-repo/BigDataProduction/ccd-release-generic/${REPO_ROOT_DIR_NAME}/doris-dependencies-${version}.tar.gz
"

  if [[ "$UPLOAD_TO_REPO" = "true" ]]; then
    version=$(get_version)
    cd $doris_tars_dir/$version

    curl -u sys_deployer:c3lzX2RlcGxveWVyCg --upload-file doris-be-${version}.tar.gz https://devops.ctcdn.cn/nexus/repository/raw-repo/BigDataProduction/ccd-release-generic/${REPO_ROOT_DIR_NAME}/doris-be-${version}.tar.gz
    check_ret_code $? "upload be"

    curl -u sys_deployer:c3lzX2RlcGxveWVyCg --upload-file doris-fe-${version}.tar.gz https://devops.ctcdn.cn/nexus/repository/raw-repo/BigDataProduction/ccd-release-generic/${REPO_ROOT_DIR_NAME}/doris-fe-${version}.tar.gz
    check_ret_code $? "upload fe"

    curl -u sys_deployer:c3lzX2RlcGxveWVyCg --upload-file doris-dependencies-${version}.tar.gz https://devops.ctcdn.cn/nexus/repository/raw-repo/BigDataProduction/ccd-release-generic/${REPO_ROOT_DIR_NAME}/doris-dependencies-${version}.tar.gz
    check_ret_code $? "upload dependencies"

    echo "success to upload to repo"
  else
    echo "not to upload to repo"
  fi
}

main() {
  check_and_set_vars

  print_info

  modify_submodule_url
  modify_src_files # TODO 临时修改

  #==============================================================================
  build_doris
  #==============================================================================
  package_list_tar
  validate_tar
  upload_to_repo
}

main
