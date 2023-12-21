# 镜像制作

## 1. 目录层级

### 1.1 arm

```
.
├── arm
│   ├── Dockerfile -> tools/doris_validation/scripts/docker/arm/Dockerfile
│   ├── doris               # 仅在镜像中进行三方件源码编译时使用
│   ├── thirdparty_src      # 仅在镜像中进行三方件源码编译时使用。可以不用，但在编译三方件时需要下载源码，耗时
│   │   ├── xxx.tar.gz
│   │   └── xxx.zip
│   ├── repository          # 仅在镜像中进行三方件源码编译时使用。可以不用，但在编译doris-thirdparty-hadoop-3.3.4.5-for-doris时需要下载jar包，耗时
│   │   ├── com
│   │   ├── org
│   │   └── xxx
│   ├── thirdparty          # 仅在直接拷贝三方件做到镜像时使用
│   │   └── installed
│   ├── jdbc
│   │   ├── mysql-connector-java-8.0.28.jar
│   │   ├── ojdbc8.jar
│   │   └── postgresql-42.5.1.jar
│   └── tools
│       ├── doris_validation
│       └── settings.xml
└── docker_build.sh -> arm/tools/doris_validation/scripts/docker/docker_build.sh
```

### 1.2 x86

```
├── x86
│   ├── Dockerfile -> tools/doris_validation/scripts/docker/x86/Dockerfile # ctyunos镜像制作
│   ├── Dockerfile_centos -> tools/doris_validation/scripts/docker/x86/Dockerfile_centos # centos镜像制作，从centos:7制作镜像，在liulian提供了harbor.ctyuncdn.cn/bdyun/basic-image/amd-centos:v1.0.0后删除该文件，使用ctyunos的Dockerfile
│   ├── doris               # 仅在镜像中进行三方件源码编译时使用
│   ├── thirdparty_src      # 仅在镜像中进行三方件源码编译时使用。可以不用，但在编译三方件时需要下载源码，耗时
│   │   ├── xxx.tar.gz
│   │   └── xxx.zip
│   ├── repository          # 仅在镜像中进行三方件源码编译时使用。可以不用，但在编译doris-thirdparty-hadoop-3.3.4.5-for-doris时需要下载jar包，耗时
│   │   ├── com
│   │   ├── org
│   │   └── xxx
│   ├── thirdparty          # 仅在直接拷贝三方件做到镜像时使用
│   │   └── installed
│   ├── jdbc
│   │   ├── mysql-connector-java-8.0.28.jar
│   │   ├── ojdbc8.jar
│   │   └── postgresql-42.5.1.jar
│   └── tools
│       ├── doris_validation
│       └── settings.xml
└── docker_build.sh -> x86/tools/doris_validation/scripts/docker/docker_build.sh
```

## 2. 制作命令

### 2.1 arm_ctyunos

```shell
sh docker_build.sh --branch=2.0.3-rc04-ctyun --from_image=harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0:v1.0.0 --output_image=harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0-doris:v1.0.0 --arch=arm --image_os=ctyunos --dockerfile=Dockerfile --download_thirdparty=false --build_image=true --push_image=false
```

### 2.2 x86_ctyunos

```shell
sh docker_build.sh --branch=2.0.3-rc04-ctyun --from_image=harbor.ctyuncdn.cn/bdyun/basic-image/amd-ctyunos:v1.0.0 --output_image=harbor.ctyuncdn.cn/bdyun/basic-image/amd-ctyunos-doris:v1.0.0 --arch=x86 --image_os=ctyunos --dockerfile=Dockerfile --download_thirdparty=false --build_image=true --push_image=false
```

### 2.3 x86_centos

```shell
sh docker_build.sh --branch=2.0.3-rc04-ctyun --from_image=centos:7 --output_image=harbor.ctyuncdn.cn/bdyun/basic-image/amd-centos-doris:v1.0.0 --arch=x86 --image_os=centos --dockerfile=Dockerfile_centos --download_thirdparty=false --download_thirdparty=false --build_image=true --push_image=false
```

