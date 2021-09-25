# [Knative入门——构建基于 Kubernetes 的现代化Serverless应用](https://www.servicemesher.com/getting-started-with-knative/)

# 1.1 译者序

## 关于 Knative

[Knative](https://github.com/knative) 是一个基于 Kubernetes 的，用于构建、部署和管理现代 serverless 应用的平台。

# 1.2 前言

Kubernetes 赢了。这不是夸大其词，事实就是如此。越来越多的人开始基于容器部署，而 Kubernetes 已经成为容器编排的事实标准。但是，Kubernetes 自己也承认，它是一个*容器*平台而不是*代码*平台。它可以作为一个运行和管理容器的很好的平台，但是这些容器是如何构建、运行、扩展和路由很大程度上是由用户自己决定的。这些是 Knative 想要补充的缺失部分。

也许您已经在生产上使用 Kubernetes，或者您是一个前沿技术爱好者，梦想着将您基于 OS/2 运行的组织现代化。不管怎样，本报告都没有假定太多东西，只是要求您知道容器是什么，具有 Kubernetes 的一些基本知识，可以访问 Kubernetes 集群。如果这些您都不具备的话，那么 Minikube 是一个很好的选择。

我们将使用大量代码示例和预先构建的容器镜像，这些镜像我们都为读者开源，您可以从 http://github.com/gswk 找到所有代码示例，并在 http://hub.docker.com/u/gswk 找到所有容器镜像。您还可以在 [http://gswkbook.com](http://gswkbook.com/) 找到这两个存储库以及其他重要参考资料的链接。

我们对 Knative 的未来十分期待。虽然我们来自 Pivotal——Knative 最大的贡献者之—— 但本报告仅出自于对 Knative 的发展前景充满期待的我们。报告中包含了我们的观点，有的读者可能不认同这些观点，还可能会热情地告诉我们为什么我们错了。没关系！这个领域非常新，并且不断重新定义自己。至少，本报告将让您思考无服务器架构（serverless），您会和我们一样对 Knative 感到兴奋。

# 1.3 Knative 概述

我们有一个信念：以平台的方式提供软件是一个最佳选择。事实证明，标准化的开发和部署流程能让开发人员更专注于新功能的研发，从而减少时间和金钱上的消耗。不仅如此，确保应用程序之间的一致性也意味着其更容易打补丁、更新和监控，从而让运维工作也更加高效。Knative 的目标就是成为这样的现代化平台。

## Knative 是什么？

我们先来看看 Knative 的目标。Knative 的目标是在基于 Kubernetes 之上为整个开发生命周期提供帮助。它的具体实现方式是：首先使你作为开发人员能够以你想要的语言和以你想要的方式来编写代码，其次帮助你构建和打包应用程序，最后帮助你运行和伸缩应用程序。

为此，Knative 将重点放在三个关键组件上：*build（构建）*你的应用程序，为其提供流量 *serving（服务）*，以及确保应用程序能够轻松地生产和消费 *event（事件）*。

*Build（构建）*

> 通过灵活的插件化的构建系统将用户源代码构建成容器。目前已经支持多个构建系统，比如 Google 的 Kaniko，它无需运行 Docker daemon 就可以在 Kubernetes 集群上构建容器镜像。

*Serving（服务）*

> 基于负载自动伸缩，包括在没有负载时缩减到零。允许你为多个修订版本（revision）应用创建流量策略，从而能够通过 URL 轻松路由到目标应用程序。

*Event（事件）*

> 使得生产和消费事件变得容易。抽象出事件源，并允许操作人员使用自己选择的消息传递层。

Knative 是以 Kubernetes 的一组自定义资源类型（CRD）的方式来安装的，因此只需使用几个 YAML 文件就可以轻松地开始使用 Knative 了。这也意味着，在本地或者托管云服务上，任何可以运行 Kubernetes 的地方都可以运行 Knative 和你的代码。

| Kubernetes 知识                                              |
| :----------------------------------------------------------- |
| 由于 Knative 是基于 Kubernetes 的一系列扩展，因此建议你先了解下 Kubernetes 和 Docker 的架构和术语。今后我们会提及以下术语，比如 namespace、Deployment、ReplicaSet 和 Pod。熟悉这些 Kubernetes 术语将帮助你在阅读时更好地理解 Knative 的基本工作。如果你对这些都不熟悉，那么这两个链接：[Kubernetes](https://kubernetes.io/docs/tutorials/kubernetes-basics/) 和 [Docker](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.12/#objectreference-v1-core) 上都有很棒的培训材料，可以直接在浏览器上阅读。 |

## 为什么是 Knative ？

除了关于无服务器架构（serverless）定义的争论之外，下一个逻辑问题是“为什么创造的是 Knative ？”随着基于容器的架构的流行和 Kubernetes 的普及，我们又开始见到一些相同的问题，这些问题之前也出现在平台即服务（PaaS）方案上并推动了其发展。如在构建容器时，我们该如何保证其一致性？谁负责给所有东西打补丁？如何根据需求来伸缩？如何实现零停机部署？

虽然 Kubernetes 确实已经演进并开始解决其中一些问题，但是之前提到的关于不断发展的无服务器架构（serverless）的概念方面产生了更多的问题。如何管理多个事件类型的一致性？如何定义事件源和目标？

许多无服务器架构（serverless）或函数即服务（FaaS）框架都尝试回答这些问题，但它们都在用不同的方式来解决问题，且不是所有的解决方案都用到了 Kubernetes。而 Knative 构建在 Kubernetes 的基础上，并为构建和部署无服务器架构（serverless）和基于事件驱动的应用程序提供了一致的标准模式。Knative 减少了这种新的软件开发方法所产生的开销，同时还把路由（routing）和事件（eventing）的复杂性抽象出来。

## 结论

现在我们已经很好地理解了 Knative 是什么以及它被创造出来的原因，接下来我们将进一步深入了解它。接下来的章节将介绍 Knative 的三个关键组件。我们将详细研究它们，并解释它们是如何协同工作的，以及如何充分发挥它们的潜力。之后，我们将了解如何在 Kubernetes 集群上安装 Knative 和一些更高级的用例。最后，我们将通过一个 demo 来展示在这个报告中你能学习到的大部分内容。











































































