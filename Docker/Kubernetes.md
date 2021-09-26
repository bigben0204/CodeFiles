

# [安装minikube](https://minikube.sigs.k8s.io/docs/start/)

```shell
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
```

## Start your cluster

From a terminal with administrator access (but not logged in as root), run:

```shell
minikube start
```

If minikube fails to start, see the [drivers page](https://minikube.sigs.k8s.io/docs/drivers/) for help setting up a compatible container or virtual-machine manager.

## Interact with your cluster

If you already have kubectl installed, you can now use it to access your shiny new cluster:

```shell
kubectl get po -A
```

Alternatively, minikube can download the appropriate version of kubectl, if you don’t mind the double-dashes in the command-line:

```shell
minikube kubectl -- get po -A
```

Initially, some services such as the storage-provisioner, may not yet be in a Running state. This is a normal condition during cluster bring-up, and will resolve itself momentarily. For additional insight into your cluster state, minikube bundles the Kubernetes Dashboard, allowing you to get easily acclimated to your new environment:

```shell
minikube dashboard
```

## Deploy applications

Create a sample deployment and expose it on port 8080:

```shell
kubectl create deployment hello-minikube --image=registry.cn-hangzhou.aliyuncs.com/google_containers/echoserver:1.4
kubectl expose deployment hello-minikube --type=NodePort --port=8080
```

It may take a moment, but your deployment will soon show up when you run:

```shell
kubectl get services hello-minikube
```

Copy

The easiest way to access this service is to let minikube launch a web browser for you:

```shell
minikube service hello-minikube
```

Alternatively, use kubectl to forward the port:

```shell
kubectl port-forward service/hello-minikube 7080:8080
```

Tada! Your application is now available at http://localhost:7080/

### LoadBalancer deployments

To access a LoadBalancer deployment, use the “minikube tunnel” command. Here is an example deployment:

```shell
kubectl create deployment balanced --image=k8s.gcr.io/echoserver:1.4  
kubectl expose deployment balanced --type=LoadBalancer --port=8080
```

In another window, start the tunnel to create a routable IP for the ‘balanced’ deployment:

```shell
minikube tunnel <service-name>  # 通过这个命令才能使LoadBalaner的服务为运行状态，否则为pending
```

To find the routable IP, run this command and examine the `EXTERNAL-IP` column:

```shell
kubectl get services balanced
```

Your deployment is now available at <EXTERNAL-IP>:8080



## Manage your cluster

Pause Kubernetes without impacting deployed applications:

```shell
minikube pause
```

Unpause a paused instance:

```shell
minikube unpause
```

Halt the cluster:

```shell
minikube stop
```

Increase the default memory limit (requires a restart):

```shell
minikube config set memory 16384
```

Browse the catalog of easily installed Kubernetes services:

```shell
minikube addons list
```

Create a second cluster running an older Kubernetes release:

```shell
minikube start -p aged --kubernetes-version=v1.16.1
```

Delete all of the minikube clusters:

```shell
minikube delete --all
```



# [学习 Kubernetes 基础知识](https://kubernetes.io/zh/docs/tutorials/kubernetes-basics/)

## [在 Linux 系统中安装 kubectl](https://kubernetes.io/zh/docs/tasks/tools/install-kubectl-linux/)

```bash
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
kubectl version --client
```

## 验证 kubectl 配置

为了让 kubectl 能发现并访问 Kubernetes 集群，你需要一个 [kubeconfig 文件](https://kubernetes.io/docs/zh/concepts/configuration/organize-cluster-access-kubeconfig/)， 该文件在 [kube-up.sh](https://github.com/kubernetes/kubernetes/blob/master/cluster/kube-up.sh) 创建集群时，或成功部署一个 Miniube 集群时，均会自动生成。 通常，kubectl 的配置信息存放于文件 `~/.kube/config` 中。

通过获取集群状态的方法，检查是否已恰当的配置了 kubectl：

```shell
kubectl cluster-info
```

如果返回一个 URL，则意味着 kubectl 成功的访问到了你的集群。

如果你看到如下所示的消息，则代表 kubectl 配置出了问题，或无法连接到 Kubernetes 集群。

```
The connection to the server <server-name:port> was refused - did you specify the right host or port?
（访问 <server-name:port> 被拒绝 - 你指定的主机和端口是否有误？）
```

例如，如果你想在自己的笔记本上（本地）运行 Kubernetes 集群，你需要先安装一个 Minikube 这样的工具，然后再重新运行上面的命令。

如果命令 `kubectl cluster-info` 返回了 url，但你还不能访问集群，那可以用以下命令来检查配置是否妥当：

```shell
kubectl cluster-info dump
```



## 创建 Deployment

Kubernetes [*Pod*](https://kubernetes.io/zh/docs/concepts/workloads/pods/) 是由一个或多个 为了管理和联网而绑定在一起的容器构成的组。 本教程中的 Pod 只有一个容器。 Kubernetes [*Deployment*](https://kubernetes.io/zh/docs/concepts/workloads/controllers/deployment/) 检查 Pod 的健康状况，并在 Pod 中的容器终止的情况下重新启动新的容器。 Deployment 是管理 Pod 创建和扩展的推荐方法。

1. 使用 `kubectl create` 命令创建管理 Pod 的 Deployment。该 Pod 根据提供的 Docker 镜像运行 Container。

   ```shell
   # kubectl create deployment hello-node --image=k8s.gcr.io/echoserver:1.4
   kubectl create deployment hello-node --image=registry.cn-hangzhou.aliyuncs.com/google_containers/echoserver:1.4
   # k8s.gcr.io/echoserver:1.4下载不下来，下载如下aliyun镜像：
   docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/echoserver:1.4
   # docker tag registry.cn-hangzhou.aliyuncs.com/google_containers/echoserver:1.4 k8s.gcr.io/echoserver:1.4 打了tag使用原命令仍会进行下载，所以不使用此命令了
   # 通过命令：kubectl describe pod 查看pod的状态
   ```

1. 查看 Deployment：

   ```shell
   kubectl get deployments
   ```

   输出结果类似于这样：

   ```
   NAME         READY   UP-TO-DATE   AVAILABLE   AGE
   hello-node   1/1     1            1           1m
   ```

1. 查看 Pod：

   ```shell
   kubectl get pods
   ```

   输出结果类似于这样：

   ```
   NAME                          READY     STATUS    RESTARTS   AGE
   hello-node-5f76cf6ccf-br9b5   1/1       Running   0          1m
   ```

1. 查看集群事件：

   ```shell
   kubectl get events
   ```

1. 查看 `kubectl` 配置：

   ```shell
   kubectl config view
   ```

## 创建 Service

默认情况下，Pod 只能通过 Kubernetes 集群中的内部 IP 地址访问。 要使得 `hello-node` 容器可以从 Kubernetes 虚拟网络的外部访问，你必须将 Pod 暴露为 Kubernetes [*Service*](https://kubernetes.io/zh/docs/concepts/services-networking/service/)。

1. 使用 `kubectl expose` 命令将 Pod 暴露给公网：

   ```shell
   kubectl expose deployment hello-node --type=LoadBalancer --port=8080
   ```

   这里的 `--type=LoadBalancer` 参数表明你希望将你的 Service 暴露到集群外部。

   镜像 `k8s.gcr.io/echoserver` 中的应用程序代码仅监听 TCP 8080 端口。 如果你用 `kubectl expose` 暴露了其它的端口，客户端将不能访问其它端口。

1. 查看你创建的 Service：

   ```shell
   kubectl get services
   ```

   输出结果类似于这样:

   ```
   NAME         TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
   hello-node   LoadBalancer   10.108.144.78   <pending>     8080:30369/TCP   21s
   kubernetes   ClusterIP      10.96.0.1       <none>        443/TCP          23m
   ```

   对于支持负载均衡器的云服务平台而言，平台将提供一个外部 IP 来访问该服务。 在 Minikube 上，`LoadBalancer` 使得服务可以通过命令 `minikube service` 访问。

1. 运行下面的命令：

   ```shell
   minikube service hello-node
   ```

## 启用插件

Minikube 有一组内置的 [插件](https://kubernetes.io/zh/docs/concepts/cluster-administration/addons/)， 可以在本地 Kubernetes 环境中启用、禁用和打开。

1. 列出当前支持的插件：

   ```shell
   minikube addons list
   ```

   输出结果类似于这样：

   ```
   addon-manager: enabled
   dashboard: enabled
   default-storageclass: enabled
   efk: disabled
   freshpod: disabled
   gvisor: disabled
   helm-tiller: disabled
   ingress: disabled
   ingress-dns: disabled
   logviewer: disabled
   metrics-server: disabled
   nvidia-driver-installer: disabled
   nvidia-gpu-device-plugin: disabled
   registry: disabled
   registry-creds: disabled
   storage-provisioner: enabled
   storage-provisioner-gluster: disabled
   ```

2. 启用插件，例如 `metrics-server`：

   ```shell
   minikube addons enable metrics-server
   ```

   输出结果类似于这样：

   ```
   The 'metrics-server' addon is enabled
   ```

3. 查看创建的 Pod 和 Service：

   ```shell
   kubectl get pod,svc -n kube-system
   ```

4. 禁用 `metrics-server`：

   ```shell
   minikube addons disable metrics-server
   ```

   输出结果类似于这样：

   ```
   metrics-server was successfully disabled
   ```

## 清理

现在可以清理你在集群中创建的资源：

```shell
kubectl delete service hello-node
kubectl delete deployment hello-node
```

可选地，停止 Minikube 虚拟机（VM）：

```shell
minikube stop
```

可选地，删除 Minikube 虚拟机（VM）：

```shell
minikube delete
```























































