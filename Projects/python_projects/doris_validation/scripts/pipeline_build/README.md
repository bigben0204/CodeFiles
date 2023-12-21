# 流水线docker构建 

## 1. 环境变量样例：
BRANCH_NAME
> 值：origin/2.0.1-rc04-ctyun
>
> 描述：构建分支
>
> 默认值：无

VERSION
> 值：2.0.1-1.0.1
>
> 描述：生成tar包的版本号
>
> 默认值：2.0.x-1.0.0

DORIS_GIT_DIR
> 值：/home/jenkins_agent/workspace/BigDataComponent_doris-arm
>
> 描述：编译的代码根目录
>
> 默认值：/home/jenkins_agent/workspace/BigDataComponent_doris-arm

CLEAN_LAST_BUILD
> 值：true/false
>
> 描述：是否清理之前编译中间结果
>
> 默认值：false

COMPILE
> 值：true/false
>
> 描述：是否编译doris
>
> 默认值：true

PACKAGE
> 值：true/false
>
> 描述：是否打成tar包
>
> 默认值：true

VALIDATE
> 值：true/false
>
> 描述：是否进行单机服务部署功能验证
>
> 默认值：true

UPLOAD_TO_REPO
> 值：false
>
> 描述：是否上传到nexus归档网站
>
> 默认值：false

REPO_ROOT_DIR_NAME
> 值：3.3.3-kunpeng-ARM-ctyunos2.0.1-tar
>
> 描述：上传到归档网站上的根目录
>
> 默认值：3.3.3-kunpeng-ARM-ctyunos2.0.1-tar

DOCKER_IMAGE_NAME
> 值：harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0-doris:v1.0.0
>
> 描述：编译时docker镜像名称
>
> 默认值：harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0-doris:v1.0.0

COMPILE_ENV
> 值：USE_UNWIND=OFF USE_AVX2=0 ENABLE_PCH=OFF
>
> 描述：运行build.sh时设置的环境变量
>
> 默认值：arm：USE_UNWIND=OFF USE_AVX2=0 ENABLE_PCH=OFF x86：无

MVN_REPOSITORY
> 值：/data1/maven
>
> 描述：宿主机的maven仓路径，映射到docker容器中为：$MVN_REPOSITORY:/root/.m2/repository。**仅在docker构建使用**
>
> 默认值：/data1/maven

RENAME_DORIS_COMPILE_DIR
> 值：true/false
>
> 描述：是否重命名doris编译目录，如果doris编译路径有中文，会导致编译失败，此时需要重命名为英文路径。**仅在虚拟机构建使用**
>
> 默认值：false

DORIS_SOFTWARE_DIR
> 值：/home/jenkins_agent/doris_software
>
> 描述：放置doris编译软件工具目录。**仅在虚拟机构建使用**
>
> 默认值：无

CUSTOM_ENV_SH_NAME
> 值：custom_env.sh
>
> 描述：custom_env.sh的名称，用于多版本构建使用。**仅在虚拟机构建使用**
>
> 默认值：custom_env.sh

## 2. 脚本中全局参数样例：
```
workspace_dir=/home/jenkins_agent/workspace
doris_tars_dir=/home/jenkins_agent/workspace/doris_tars
```


## 3. 前置准备：
```
/home/jenkins_agent/doris_software/custom_env_files/custom_env.sh # 虚拟机使用，docker镜像中已配置为环境变量
/home/jenkins_agent/doris_software/software/ldb_toolchain # 已在docker镜像里
/home/jenkins_agent/doris_software/software/bisheng-jdk1.8.0_352 # 已在docker镜像里
/home/jenkins_agent/doris_software/software/jdbc # 打包使用，已在docker镜像里
/home/jenkins_agent/doris_software/software/doris_validation # 验证使用，已在docker镜像里
/home/jenkins_agent/doris_software/software/thirdparty # 该目录中有子目录installed，已在docker镜像里
```
其中custom_env.sh内容样例为：
```shell
#!/bin/bash

export JAVA_HOME=/home/jenkins_agent/doris_software/software/bisheng-jdk1.8.0_352
export PATH=$JAVA_HOME/bin:$PATH
export PATH=/home/jenkins_agent/doris_software/software/apache-maven-3.6.3/bin:$PATH
export PATH=/home/jenkins_agent/doris_software/software/node-v12.13.0-linux-x64/bin:$PATH
export PATH=/home/jenkins_agent/doris_software/software/ldb_toolchain/bin:$PATH
export DORIS_THIRDPARTY=/home/jenkins_agent/doris_software/software/thirdparty
```

## 4. 调用样例：

### 4.1 docker构建

#### 4.1.1 arm_ctyunos

`DORIS_GIT_DIR=/home/jenkins_agent/workspace/BigDataComponent_doris-arm VERSION=2.0.1-1.0.1 CLEAN_LAST_BUILD=false COMPILE=true PACKAGE=true VALIDATE=true UPLOAD_TO_REPO=false DOCKER_IMAGE_NAME=harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0-doris:v1.0.0 COMPILE_ENV="USE_UNWIND=OFF USE_AVX2=0 ENABLE_PCH=OFF" MVN_REPOSITORY=/data1/maven REPO_ROOT_DIR_NAME=3.3.3-kunpeng-ARM-ctyunos2.0.1-newtar sh pipeline_build_docker_compile.sh > build.log 2>&1`

#### 4.1.2 x86_ctyunos

`DORIS_GIT_DIR=/data1/doris/doris_software/temp/doris VERSION=2.0.1-1.0.1 CLEAN_LAST_BUILD=false COMPILE=true PACKAGE=true VALIDATE=true UPLOAD_TO_REPO=false DOCKER_IMAGE_NAME=harbor.ctyuncdn.cn/bdyun/basic-image/amd-ctyunos-doris:v1.0.0 COMPILE_ENV="" MVN_REPOSITORY=/data1/maven REPO_ROOT_DIR_NAME=3.3.3-intel-x86-ctyunos2.0.1-newtar sh pipeline_build_docker_compile.sh > build.log 2>&1`

#### 4.1.3 x86_centos

`DORIS_GIT_DIR=/data1/doris/doris_software/temp/doris VERSION=2.0.1-1.0.1 CLEAN_LAST_BUILD=false COMPILE=true PACKAGE=true VALIDATE=true UPLOAD_TO_REPO=false DOCKER_IMAGE_NAME=harbor.ctyuncdn.cn/bdyun/basic-image/amd-centos-doris:v1.0.0 COMPILE_ENV="" MVN_REPOSITORY=/data1/maven REPO_ROOT_DIR_NAME=3.3.3-x86-centos7-tar sh pipeline_build_docker_compile.sh > build.log 2>&1`

### 4.2 虚拟机构建

#### 4.2.1 arm_ctyunos

`DORIS_GIT_DIR=/data1/doris/doris_software/temp/doris DORIS_SOFTWARE_DIR=/data1/doris/doris_software CUSTOM_ENV_SH_NAME=custom_env.sh VERSION=2.0.1-1.0.1 CLEAN_LAST_BUILD=false COMPILE=true PACKAGE=true VALIDATE=true UPLOAD_TO_REPO=false COMPILE_ENV="USE_UNWIND=OFF USE_AVX2=0 ENABLE_PCH=OFF" RENAME_DORIS_COMPILE_DIR=false REPO_ROOT_DIR_NAME=3.3.3-kunpeng-ARM-ctyunos2.0.1-newtar sh pipeline_build.sh > build.log 2>&1`

#### 4.2.2 x86_ctyunos

`DORIS_GIT_DIR=/data1/doris/doris_software/temp/doris DORIS_SOFTWARE_DIR=/data1/doris/doris_software CUSTOM_ENV_SH_NAME=custom_env.sh VERSION=2.0.1-1.0.1 CLEAN_LAST_BUILD=false COMPILE=true PACKAGE=true VALIDATE=true UPLOAD_TO_REPO=false COMPILE_ENV="" RENAME_DORIS_COMPILE_DIR=false REPO_ROOT_DIR_NAME=3.3.3-intel-x86-ctyunos2.0.1-newtar sh pipeline_build.sh > build.log 2>&1`

#### 4.2.3 x86_centos

`DORIS_GIT_DIR=/data1/doris/doris_software/temp/doris DORIS_SOFTWARE_DIR=/data1/doris/doris_software CUSTOM_ENV_SH_NAME=custom_env.sh VERSION=2.0.1-1.0.1 CLEAN_LAST_BUILD=false COMPILE=true PACKAGE=true VALIDATE=true UPLOAD_TO_REPO=false COMPILE_ENV="" RENAME_DORIS_COMPILE_DIR=false REPO_ROOT_DIR_NAME=3.3.3-x86-centos7-tar sh pipeline_build.sh > build.log 2>&1`