

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

Your deployment is now available at `http://<EXTERNAL-IP>:8080`



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

# [你好，Minikube](https://kubernetes.io/zh/docs/tutorials/hello-minikube/)

## 创建 Deployment

Kubernetes [*Pod*](https://kubernetes.io/zh/docs/concepts/workloads/pods/) 是由一个或多个 为了管理和联网而绑定在一起的容器构成的组。 本教程中的 Pod 只有一个容器。 Kubernetes [*Deployment*](https://kubernetes.io/zh/docs/concepts/workloads/controllers/deployment/) 检查 Pod 的健康状况，并在 Pod 中的容器终止的情况下重新启动新的容器。 Deployment 是管理 Pod 创建和扩展的推荐方法。

1.  使用 `kubectl create` 命令创建管理 Pod 的 Deployment。该 Pod 根据提供的 Docker 镜像运行 Container。

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

# [使用服务来访问集群中的应用](https://kubernetes.io/zh/docs/tasks/access-application-cluster/service-access-application-cluster/)

## 教程目标

- 运行 Hello World 应用的两个实例。
- 创建一个服务对象来暴露 node port。
- 使用服务对象来访问正在运行的应用。

## 为运行在两个 pod 中的应用创建一个服务

这是应用程序部署的配置文件：

[`service/access/hello-application.yaml` ](https://raw.githubusercontent.com/kubernetes/website/main/content/zh/examples/service/access/hello-application.yaml)![Copy service/access/hello-application.yaml to clipboard](https://d33wubrfki0l68.cloudfront.net/0901162ab78eb4ff2e9e5dc8b17c3824befc91a6/44ccd/images/copycode.svg)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-world
spec:
  selector:
    matchLabels:
      run: load-balancer-example
  replicas: 2
  template:
    metadata:
      labels:
        run: load-balancer-example
    spec:
      containers:
        - name: hello-world
          image: gcr.io/google-samples/node-hello:1.0
          ports:
            - containerPort: 8080
              protocol: TCP
```

1. 在你的集群中运行一个 Hello World 应用： 使用上面的文件创建应用程序 Deployment：

   ```shell
   # kubectl apply -f https://k8s.io/examples/service/access/hello-application.yaml
   # 使用本地文件，并修改image为zhangguanzhang/gcr.io.google-samples.node-hello:1.0，如下
   kubectl apply -f hello-application.yaml
   # 界面回显：
   deployment.apps/hello-world created
   ```
   
   上面的命令创建一个 [Deployment](https://kubernetes.io/zh/docs/concepts/workloads/controllers/deployment/) 对象 和一个关联的 [ReplicaSet](https://kubernetes.io/zh/docs/concepts/workloads/controllers/replicaset/) 对象。 这个 ReplicaSet 有两个 [Pod](https://kubernetes.io/zh/docs/concepts/workloads/pods/)， 每个 Pod 都运行着 Hello World 应用。
   
   hello-application.yaml内容如下：
   
   ```yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: hello-world
   spec:
     selector:
       matchLabels:
         run: load-balancer-example
     replicas: 2
     template:
       metadata:
         labels:
           run: load-balancer-example
       spec:
         containers:
           - name: hello-world
             image: gcr.io/google-samples/node-hello:1.0
             ports:
               - containerPort: 8080
                 protocol: TCP
   ```
   
2. 展示 Deployment 的信息：

   ```shell
   kubectl get deployments hello-world
   kubectl describe deployments hello-world
   ```

3. 展示你的 ReplicaSet 对象信息：

   ```shell
   kubectl get replicasets
   kubectl describe replicasets
   ```

4. 创建一个服务对象来暴露 Deployment：

   ```shell
   kubectl expose deployment hello-world --type=NodePort --name=example-service
   ```

   可以配置hello-service.yaml：

   ```yaml
   apiVersion: v1
   kind: Service
   metadata:
     name: example-service
   spec:
     type: NodePort
     selector:
       run: load-balancer-example
     ports:
       - protocol: TCP
         port: 8080
         #targetPort: 80
         #nodePort: 32222
   ```

   并通过如下命令启动：

   ```shell
   kubectl apply -f hello-service.yaml
   ```

5. 展示 Service 信息：

   ```shell
   kubectl describe services example-service
   ```

   输出类似于：

   ```shell
   Name:                     example-service
   Namespace:                default
   Labels:                   <none>
   Annotations:              <none>
   Selector:                 run=load-balancer-example
   Type:                     NodePort
   IP Family Policy:         SingleStack
   IP Families:              IPv4
   IP:                       10.98.187.149
   IPs:                      10.98.187.149
   Port:                     <unset>  8080/TCP
   TargetPort:               8080/TCP
   NodePort:                 <unset>  31417/TCP
   Endpoints:                172.17.0.3:8080,172.17.0.4:8080
   Session Affinity:         None
   External Traffic Policy:  Cluster
   Events:                   <none>
   ```

   注意服务中的 NodePort 值。例如在上面的输出中，NodePort 是 31417。

   或通过：

   ```shell
   kubectl get service -o wide
   ```

   输出：

   ```shell
   example-service   NodePort    10.98.187.149   <none>        8080:31417/TCP   10m   run=load-balancer-example
   kubernetes        ClusterIP   10.96.0.1       <none>        443/TCP          70m   <none>
   ```

6. 列出运行 Hello World 应用的 Pod：

   ```shell
   kubectl get pods --selector="run=load-balancer-example" --output=wide
   ```

   输出类似于：

   ```shell
   NAME                           READY   STATUS    ...  IP           NODE
   hello-world-2895499144-bsbk5   1/1     Running   ...  10.200.1.4   worker1
   hello-world-2895499144-m1pwt   1/1     Running   ...  10.200.2.5   worker2
   ```

7. 获取运行 Hello World 的 pod 的其中一个节点的公共 IP 地址。如何获得此地址取决于你设置集群的方式。 例如，如果你使用的是 Minikube，则可以通过运行 `kubectl cluster-info` 来查看节点地址。 如果你使用的是 Google Compute Engine 实例，则可以使用 `gcloud compute instances list` 命令查看节点的公共地址。

   输出：

   ```shell
   ben@ben-virtual-machine:~/Softwares/kubectl/config$ kubectl cluster-info
   Kubernetes control plane is running at https://192.168.49.2:8443
   CoreDNS is running at https://192.168.49.2:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
   
   To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
   ```

   192.168.49.2 即为公共IP。

   

8. 在你选择的节点上，创建一个防火墙规则以开放节点端口上的 TCP 流量。 例如，如果你的服务的 NodePort 值为 31568，请创建一个防火墙规则以允许 31568 端口上的 TCP 流量。 不同的云提供商提供了不同方法来配置防火墙规则。

9. 使用节点地址和 node port 来访问 Hello World 应用：

    ```shell
    curl http://<public-node-ip>:<node-port>
    #本例为：
    curl http://192.168.49.2:31417
    Hello Kubernetes!
    ```

    这里的 `<public-node-ip>` 是你节点的公共 IP 地址，`<node-port>` 是你服务的 NodePort 值。 对于请求成功的响应是一个 hello 消息：

    ```shell
    Hello Kubernetes!
    ```

    通过如下命令，也可以查看访问地址：

    ```shell
    ben@ben-virtual-machine:~/Softwares/kubectl/config$ minikube service example-service
    |-----------|-----------------|-------------|---------------------------|
    | NAMESPACE |      NAME       | TARGET PORT |            URL            |
    |-----------|-----------------|-------------|---------------------------|
    | default   | example-service |        8080 | http://192.168.49.2:31417 |
    |-----------|-----------------|-------------|---------------------------|
    * Opening service default/example-service in default browser...
    ```

## 使用服务配置文件

作为 `kubectl expose` 的替代方法，你可以使用 [服务配置文件](https://kubernetes.io/zh/docs/concepts/services-networking/service/) 来创建服务。

## 清理现场

想要删除服务，输入以下命令：

```shell
kubectl delete services example-service
# 或
kubectl delete -f hello-service.yaml
```

想要删除运行 Hello World 应用的 Deployment、ReplicaSet 和 Pod，输入以下命令：

```shell
kubectl delete deployment hello-world
# 或
kubectl delete -f hello-application.yaml
```

-----

通过 kubectl describe 可以查看example-service 与 Pod 的对应关系。<https://www.cnblogs.com/shuaiyin/p/11070096.html>

```shell
kubectl describe service example-service
kubectl get pod -o wide
```

输出：

```shell
ben@ben-virtual-machine:~/Softwares/kubectl/config$ kubectl describe service example-service
Name:                     example-service
Namespace:                default
Labels:                   <none>
Annotations:              <none>
Selector:                 run=load-balancer-example
Type:                     NodePort
IP Family Policy:         SingleStack
IP Families:              IPv4
IP:                       10.98.187.149
IPs:                      10.98.187.149
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  31417/TCP
Endpoints:                172.17.0.3:8080,172.17.0.4:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>
ben@ben-virtual-machine:~/Softwares/kubectl/config$ kubectl get pod -o wide
NAME                           READY   STATUS    RESTARTS   AGE   IP           NODE       NOMINATED NODE   READINESS GATES
hello-world-5d4489bdb7-4jm77   1/1     Running   0          21m   172.17.0.3   minikube   <none>           <none>
hello-world-5d4489bdb7-r8lcp   1/1     Running   0          21m   172.17.0.4   minikube   <none>           <none>
```

Endpoints 罗列了两个 Pod 的 IP 和端口。我们知道 Pod 的 IP 是在容器中配置的，那么 Service 的 Cluster IP 又是配置在哪里的呢？CLUSTER-IP 又是如何映射到 Pod IP 的呢？

答案是 iptables

-------------

























