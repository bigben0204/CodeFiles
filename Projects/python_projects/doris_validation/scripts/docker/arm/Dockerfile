# docker build --build-arg from_image=harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0-doris:v1.0.0 -t harbor.ctyuncdn.cn/bdyun/basic-image/arm-ctyunos-1.0.0-doris:v1.0.0 -f Dockerfile .

ARG from_image
FROM ${from_image} AS builder

ARG arch
ARG image_os

# install dependencies
RUN yum install -y byacc patch automake libtool make which file ncurses-devel gettext-devel unzip bzip2 zip util-linux wget git python2
RUN yum install -y hostname mysql5 sudo
RUN mkdir -p /etc/my.cnf.d

# clean cache
RUN yum clean all

ENV TOOLS_DIR="/tmp/tools"
COPY tools $TOOLS_DIR
# install bisheng jdk
#RUN mkdir -p /usr/local/jdk && \
#    wget -q -O /tmp/bisheng-jdk.tar.gz https://mirrors.huaweicloud.com/kunpeng/archive/compiler/bisheng_jdk/bisheng-jdk-8u352-linux-aarch64.tar.gz && \
#    tar -xzf /tmp/bisheng-jdk.tar.gz -C /usr/local/jdk && \
#    mv /usr/local/jdk/bisheng-jdk1.8.0_352 /usr/local/jdk/bisheng-jdk && \
#    rm -f /tmp/bisheng-jdk.tar.gz
#RUN mkdir -p /usr/local/jdk && \
#    tar -xzf $TOOLS_DIR/bisheng-jdk-8u352-linux-aarch64.tar.gz -C /usr/local/jdk && \
#    mv /usr/local/jdk/bisheng-jdk1.8.0_352 /usr/local/jdk/bisheng-jdk

# install maven 3.6.3
#RUN mkdir -p /usr/share/maven /usr/share/maven/ref && \
#    wget -q -O /tmp/apache-maven.tar.gz https://doris-thirdparty-repo.bj.bcebos.com/thirdparty/apache-maven-3.6.3-bin.tar.gz && \
#    tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 && \
#    rm -f /tmp/apache-maven.tar.gz && \
#    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
RUN mv $TOOLS_DIR/settings.xml /usr/local/maven/conf/settings.xml
#RUN mkdir -p /usr/share/maven /usr/share/maven/ref && \
#    tar -xzf $TOOLS_DIR/apache-maven-3.6.3-bin.tar.gz -C /usr/share/maven --strip-components=1 && \
#    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
#RUN mv $TOOLS_DIR/settings.xml /usr/local/maven/conf/settings.xml

# install nodejs
#RUN wget https://doris-thirdparty-repo.bj.bcebos.com/thirdparty/node-v16.3.0-linux-arm64.tar.xz \
#    -q -O /tmp/node.tar.xz \
#    && cd /tmp/ && tar -xf node.tar.xz \
#    && cp -r node-v16.3.0-linux-arm64/* /usr/local/ \
#    && rm /tmp/node.tar.xz && rm -rf node-v16.3.0-linux-arm64
#RUN cd $TOOLS_DIR && tar -xf node-v16.3.0-linux-arm64.tar.xz \
#    && cp -r node-v16.3.0-linux-arm64/* /usr/local/

# install ldb-toolchain
RUN wget http://custom-release.obs.cn-north-4.myhuaweicloud.com/ldb_toolchain_gen_aarch64.sh \
    -q -O /tmp/ldb_toolchain_gen_aarch64.sh \
    && sh /tmp/ldb_toolchain_gen_aarch64.sh /var/local/ldb-toolchain/ \
    && rm /tmp/ldb_toolchain_gen_aarch64.sh
#RUN sh $TOOLS_DIR/ldb_toolchain_gen_aarch64.sh /var/local/ldb-toolchain/

# copy doris_validation_project to validate tars
RUN rm -rf $TOOLS_DIR/doris_validation/.git && mv $TOOLS_DIR/doris_validation /var/local

RUN rm -rf $TOOLS_DIR

COPY jdbc /var/local/jdbc

# there is a repo which is included all of thirdparty
#ENV JAVA_HOME="/usr/local/jdk/bisheng-jdk"
#ENV REPOSITORY_URL="https://doris-thirdparty-repo.bj.bcebos.com/thirdparty/" \
#    DEFAULT_DIR="/var/local" \
#    PATH="/var/local/ldb-toolchain/bin/:$JAVA_HOME/bin:$PATH" \
#    USE_AVX2=0
ENV REPOSITORY_URL="https://doris-thirdparty-hk-1308700295.cos.ap-hongkong.myqcloud.com/thirdparty" \
    DEFAULT_DIR="/var/local" \
    PATH="/var/local/ldb-toolchain/bin/:/var/local/thirdparty/installed/bin/:$PATH" \
    DORIS_THIRDPARTY="/var/local/thirdparty" \
    USE_AVX2=0

# disable auto enable ccache
RUN rm -f /etc/profile.d/ccache.*

# clone lastest source code, download and build third party
COPY doris ${DEFAULT_DIR}/doris
# thirdparty packages is best to be downloaded in case of long time downloading in docker
COPY thirdparty_src ${DEFAULT_DIR}/doris/thirdparty/src
# copy jars for doris-thirdparty-hadoop-3.3.4.5-for-doris
RUN mkdir -p /root/.m2
COPY repository /root/.m2/repository
# curl: (77) if not set --insecure
RUN echo "--insecure" > /root/.curlrc
# temporary "doris-thirdparty-hadoop-3.3.4.5-for-doris" in arm need to add -march=armv8-a+crc in GCC_SHARED_FLAGS
RUN sed -i '/cd "${TP_SOURCE_DIR\}\/${HADOOP_LIBS_SOURCE}"/a\sed -i "s/\\(set(GCC_SHARED_FLAGS.*64\\)\\")/\\1 -march=armv8-a+crc\\")/g" hadoop-common-project/hadoop-common/HadoopCommon.cmake' ${DEFAULT_DIR}/doris/thirdparty/build-thirdparty.sh

RUN cd ${DEFAULT_DIR}/doris && /bin/bash thirdparty/build-thirdparty.sh \
    && rm -rf ${DEFAULT_DIR}/doris/thirdparty/src \
    && rm -rf ${DEFAULT_DIR}/doris-thirdparty.tar.gz \
    && rm -rf ${DEFAULT_DIR}/doris-thirdparty \
    && mkdir -p ${DEFAULT_DIR}/thirdparty \
    && mv ${DEFAULT_DIR}/doris/thirdparty/installed ${DEFAULT_DIR}/thirdparty/ \
    && rm -rf ${DEFAULT_DIR}/doris
#COPY thirdpary installed dir in case of long time compiling in docker
#COPY thirdparty/installed ${DEFAULT_DIR}/thirdparty/installed

# delete jars downloaded by build-thirdparty
RUN rm -rf /root/.m2/repository

# squash all layers to reduce the image size
FROM scratch
COPY --from=builder / /
ENV JAVA_HOME="/path/to/jdk" \
    MAVEN_HOME="/usr/local/maven"
ENV REPOSITORY_URL="https://doris-thirdparty-repo.bj.bcebos.com/thirdparty" \
    DEFAULT_DIR="/var/local" \
    PATH="/var/local/ldb-toolchain/bin/:/var/local/thirdparty/installed/bin/:$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH" \
    DORIS_THIRDPARTY="/var/local/thirdparty" \
    USE_AVX2=0
#ENV REPOSITORY_URL="https://doris-thirdparty-repo.bj.bcebos.com/thirdparty" \
#    DEFAULT_DIR="/var/local" \
#    PATH="/var/local/ldb-toolchain/bin/:/var/local/thirdparty/installed/bin/:$PATH" \
#    DORIS_THIRDPARTY="/var/local/thirdparty" \
#    USE_AVX2=0
WORKDIR /root

CMD ["/bin/bash"]
