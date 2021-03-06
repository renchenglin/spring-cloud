#传统的分布式架构
传统的分布式架构一般使用路由转发的模式进行负载均衡或者反向代理实现，通过虚拟路由冗余VRRP协议实现代理服务器或者负载均衡器的高可用。这种模型适用于在企业数据中心内部运行的应用程序，但是对于微服务应用程序来说，这种模型并不适合，原因有以下几个。

*	单点故障：虽然负载均衡可以实现高可用（VRRP），但这是整个基础设施的单点故障。如果负载均衡出现故障，那么依赖它的所有的应用都会出现故障。尽管可以使负载均衡高度可用，但负载均衡器往往是应用程序基础设施中的集中式阻塞点。

*	有限的水平可伸缩性：在服务集中到负载均衡器集群的情况下，跨多个服务器水平伸缩负载基础设施的能力有限。

*	静态管理：大多数传统的负载均衡器不是为快速注册和注销服务设计的。它们使用集中式数据库来存储路由规则，添加新路由的唯一方式通常是通过供应商的专有API来进行添加

*	复杂：负载均衡充当代理，它必须将服务消费者的请求映射到物理服务，这层配置必须开发人员手动定义和部署

#基于云的服务发现机制的特点

*	高可用：服务发现需要支持热集群环境，可以跨多个节点共享服务查找，如果一个节点不可以，集群中的其他节点应该能够接管工作

*	点对点：服务发现集群中的每个节点共享服务实例的状态

*	负载均衡：服务发现需要在所有服务实例之间动态的对请求进行负载均衡，以确保服务调用分布到由它管理的所有服务实例上

*	有弹性：服务发现的客户端应该本地缓存服务信息

*	容错：服务发现需要检测出服务实例什么时候不健康，并及时将其进行剔除。

