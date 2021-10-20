# 删除

docker rm $(docker ps -qf status=exited)

# 镜像下载不下来

参考<https://blog.csdn.net/weixin_30505043/article/details/97452937?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.no_search_link>

## 使用国内作者制作的gcr.io镜像安装工具

[项目地址:](https://github.com/zhangguanzhang/gcr.io) https://github.com/zhangguanzhang/gcr.io

### **查询namespace**

curl -s https://zhangguanzhang.github.io/bash/pull.sh | bash -s search gcr.io

### **查询某一名称空间下镜像列表**

curl -s https://zhangguanzhang.github.io/bash/pull.sh | bash -s search gcr.io/google-samples

### **查询某一镜像的版本所有版本tag**

curl -s https://zhangguanzhang.github.io/bash/pull.sh | bash -s search gcr.io/google-samples/node-hello

### 拉取镜像

curl -s https://zhangguanzhang.github.io/bash/pull.sh | bash -s -- gcr.io/google-samples/node-hello:1.0

docker pull zhangguanzhang/gcr.io.google-samples.node-hello:1.0

