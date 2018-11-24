# 消息中间件

*	平台无关性

*	解耦：消息中间件在处理过程中插入了一个隐含的、基于数据的接口层，两边的处理过程都要基于这一接口，这允许独立的修改或者扩展两边的接口，只要确保都遵循同样的接口规范即可。

*	冗余（存储）：有些情况下，处理数据的过程会失败。消息中间件提供了持久化直至数据被完全处理，规避了数据丢失的风险。

*	扩展性：

*	削峰：在访问量剧增的情况下，消息中间件能够使关键组件支撑突发的访问压力。突发的访问压力并不常见，对于投入资源无疑是巨大的浪费。

*	队列：在一定程度上可以保证顺序。

*	异步通信：消息中间件提供了异步处理机制，允许应用把一些消息放入消息中间件中，但并不立即处理，在之后的时间进行慢慢处理。


# RabbitMQ
	rabbitmq是Client/Server模式，其中根据消息的不同行为，Client又分为生产者和消费者。
	生产者生成消息，发送到rabbitmq（又称为broker），broker经过内部路由转发（通过交换器根据交换器类型、路由键、绑定等进行路由转发）到队列。
	消费者通过订阅指定的队列，获取broker转发的消息。
	
## 队列（Queue）

*	在rabbitmq中的内部对象，用于存储消息，生产者产生的消息最终投递到队列中。
*	消费者只能从队列中获取消息
*	多个消费者可以订阅同一个队列，这时队列中的消息会被平均分摊（rount-robbin,轮询）
*	队列不支持广播

## 交换器（Exchange）

*	生产者的消息最先到达交换器，由交换机进行消息路由，转发到队列中（如果路由不到则丢弃或者返回给生产者，具体见mandatory的配置）
*	交换器有四种类型（fanout、direct、topic、headers）
*	交换器与队列绑定，即为交换器指定路由规则
*	交换器也可以直接和交换器绑定，与跟队列绑定没有区别，上一个交换器会根据规则路由到下一个交换器。rabbitmq的消息存储在队列中，交换器的使用不会耗性能，这个是交换器绑定的优势
*	default交换器是一个direct类型的交换器

## 路由键（RoutingKey）

*	生产者将消息发给交换器时，需要指定一个 RoutingKey，用来指定消息的路由规则

## 绑定（Bingding）

*	交换器和队列通过绑定进行关联，指定交换器与队列的联系关系
*	交换器的路由转发由交换器的模式、消息指定的路由键、交换器的绑定三者决定的
*	对于topic类型的交换器，RoutingKey与BindingKey之间需要做模糊匹配

## 交换器类型

*	fanout:将发往该交换器的消息路由到所有绑定到该交换器的队列中，也就是说此时RoutingKey失效。
*	direct:将发往该交换器的消息路由到绑定到该交换器中BindingKey与消息的RoutingKey完全一致的队列中
*	topic:将消息路由到RoutingKey与BindingKey相匹配的队列中，可以使用*或者#进行模糊匹配，即将一个路由键路由到
多个队列中
*	headers:headers类型的交换器不依赖于路由键的匹配，而是根据发送的消息内容中的headers属性进行完全匹配，headers性能很差，而且也不实用，现在基本不用了。



# 消费消息的两种模式

*	推模式

*	拉模式


# 消息确认机制
	为了保证消息从队列可靠地到达消费者，rabbitmq提供了消息确认机制。
	消费者在订阅队列时，可以通过设置autoAck参数来选择消息的确认模式。
*	当autoAck=true时，rabbitmq会自动把发送出去的消息置为确认，然后将消息移除，不管消息是否真正到达消费者。
*	当autoAck=false时，rabbitmq会等待消费者显式回复确认信号时，才会将消息移除（先打上删除标记，之后再删除）
*	当autoAck=false时，队列中的消息分为两个部分：已经投递待确认的消息，未投递的消息
*	如果已经投递的消息一直没有收到确认，并且消费此消息的消费者已经断开连接，则该消息会进行重新投递，进入到未投递的消息中
*	未确认的消息不计算TTL，会一直等到消费者的确认，或者消费者断开连接后的重新投递。


# mandatory

*	当mandatory设置为true时，交换器无法根据自身类型和路由键找到一个符合条件的队列时，会调用Basic.Return命令将消息返回给生产者。
*	当mandatory设置为false时，交换器无法根据自身类型和路由键找到一个符合条件的队列时，直接将消息丢弃

*	immediate：immediate是与mandatory一级的参数，3.0版本已经被启用，改用TTL和DLX的方法替代

# 备份交换器
	备份交换器（Alternate Exchange），生产者在发送消息时如果不设置mandatory参数，那么消息在未被路由的时候会被丢失，如果设置mandatory，那么需要设置回调的处理逻辑。
	如果不想进行回调设置使逻辑变得复杂，又不丢失消息数据，备份交换器可以解决这个问题。


# TTL
	Time To Live,即过期时间。

*	可以针对消息和队列两个维度设置过期时间。

*	通过队列设置TTL，针对的是队列中的所有消息，所有消息都有相同的TTL，消息过期会立即从队列中抹除（判断消息过期，每条消息都有相同的过期时间，只需要定期从头部扫描即可）
*	也可以在消息投递时设置消息的TTL，单位是毫秒，只有在投递之前，才能判断是过期消息（因为判断消息过期，需要扫描整个队列）
*	如果两种方式都设置，则以TTL较小的时间为准
*	TTL不设置，则表示消息永不过期
*	TTL设置为0，则表示具体立即投递消息的条件则会立即将消息投递给消费者，否则会立即被丢弃
*	队列的TTL不能设置为0

# DLX
	Dead-Letter-Exchange,死信交换器。队列中的消息会变为死信（Dead Message），变为死信之后，它能被重新发送到另一个交换器中，这个交换器就是DLX，与DLX绑定的队列为死信队列。

*	变为死信一般有几个情况：消息被拒绝，并且设置requeue为false；消息过期（TTL超时）；队列到达最大长度

# 延迟队列
	延迟队列存储的对象是对应的延迟消息，所谓“延迟消息”是指当消息被发送以后，并不想让消费者立即拿到消息，而是等到特定时间后，消费者才能拿到进行消费，如支付场景一般会有30min中的支付时间，30分钟不支付，则进入异常处理，在这个场景下，异常处理的消息即为延迟队列。

*	RabbitMQ不支持延迟队列，但是通过TTL和死信队列可以完成延迟队列的功能。

# 持久化
	持久化可以提高RabbitMQ的可靠性，以防在异常情况（重启、宕机、关闭等）下的数据丢失。

*	持久化分为三个部分：交换器持久化、队列持久化、消息持久化
*	申明队列时将durable设置为true，如果交换器不设置持久化，那么在rabbitmq重启时。相关的交换器的元数据会丢失，交换器无法进行路由
*	队列不设置持久化，重启时，队列的元数据会丢失，此时消息也会丢失。
*	队列的持久化只能保证队列的元数据，但是并不能保证内部存储的消息不会丢失，要确保消息不会丢失，则也必须将消息也设置为持久化
*	如果消费者在订阅消息将autoAck参数设置为true，当消费者接收到消息还没有来得及处理就宕机了，也会丢失消息。解决办法是autoAck设置为false


# vhost
	虚拟主机（virtual host），简称vhost

*	每一个vhost本质上是一个独立的小型rabbitmq，拥有自己的交换器、绑定关系、队列，并且拥有独立的权限控制。
*	vhost之间的资源隔离是绝对隔离，无法将vhost1中的队列绑定到vhost2的交换器上。
*	rabbitmq的权限控制是指在vhost级别对用户而言的权限授予，通常创建用户时会指定用户管理的vhost
*	详见：rabbitmqctl set permissions [-p vhost] {user) {conf) {write) {read)
	其 中各个参数的含义如下所述。
	vhost 授予用户访问权限的 host 名称，可以设置为默认值，即 vhost为"/"
	user: 可以访问指定 vhost 的用户名。
	conf: 一个用于匹配用户在哪些资源上拥有可配置权限的正则表达式。
	write: 一个用于匹配用户在哪些资源上拥有可写权限的正则表达式。
	read 个用于匹配用户在哪些资源上拥有可读权限的正则表达式






# 安装

```

docker pull rabbitmq:3.7.8-management
docker run -d --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3.7.8-management

运行容器
docker run -d  -p 5671:5671 -p 5672:5672  -p 15672:15672 -p 15671:15671  -p 25672:25672  -v /root/data/rabbitmq-data/:/var/rabbitmq/lib  --name rabbitmq rabbitmq:3.7.8-management

访问管理端
http://192.168.31.105:15672

进入容器
docker exec -it rabbitmq /bin/bash

```

# 使用docker compose的方式进行docker容器管理

```
创建并启动容器
docker-compose -f ./docker-compose.yml up

停止并删除容器
docker-compose -f ./docker-compose.yml down

docker-compose -f ./docker-compose.yml start

docker-compose -f ./docker-compose.yml stop

docker-compose -f ./docker-compose.yml restart
```


# rabbitmq集群

## 集群模式
	rabbitmq有3种模式，其中集群模式是2种

*	单一模式：即单机情况不做集群，就单独运行一个rabbitmq而已。
*	普通模式：默认模式，以两个节点（rabbit01、rabbit02）为例来进行说明。
	对于Queue来说，消息实体只存在于其中一个节点rabbit01（或者rabbit02），rabbit01和rabbit02两个节点仅有相同的元数据，即队列的结构。
	当消息进入rabbit01节点的Queue后，consumer从rabbit02节点消费时，RabbitMQ会临时在rabbit01、rabbit02间进行消息传输，把A中的消息实体取出并经过B发送给consumer。
	所以consumer应尽量连接每一个节点，从中取消息。即对于同一个逻辑队列，要在多个节点建立物理Queue。
	否则无论consumer连rabbit01或rabbit02，出口总在rabbit01，会产生瓶颈。
	当rabbit01节点故障后，rabbit02节点无法取到rabbit01节点中还未消费的消息实体。
	如果做了消息持久化，那么得等rabbit01节点恢复，然后才可被消费；如果没有持久化的话，就会产生消息丢失的现象。
*	镜像模式:把需要的队列做成镜像队列，存在与多个节点属于RabbitMQ的HA方案。该模式解决了普通模式中的问题，其实质和普通模式不同之处在于，消息实体会主动在镜像节点间同步，而不是在客户端取数据时临时拉取。
	该模式带来的副作用也很明显，除了降低系统性能外，如果镜像队列数量过多，加之大量的消息进入，集群内部的网络带宽将会被这种同步通讯大大消耗掉。
	所以在对可靠性要求较高的场合中适用。
	
参考：https://www.cnblogs.com/knowledgesea/archive/2017/03/11/6535766.html

## 集群节点类型
	在使用 rabbitrnqctl cluster status 命令来查看集群状态，
	这时会有 {nodes [{disc , [rabbit@nodel , rabbit@node2 , rabbit@node3]}]这一项信息。
	disc标注的是集群节点类型（根据队列、交换器、绑定关系、用户、权限、vhost等的存储方式分为磁盘节点和内存节点），集群中节点为了获得较好的性能，可以选择内存节点。
	rabbitmqctl join_cluster rabbit@nodel --ram 表示添加为内存节点。

## 集群节点剔除
	
*	方式一：
		1、首先在node2节 点上执行 rabbitmqctl stop_app 或者 rabbitmqctl stop 命令来关闭 RabbitMQ服务。
		2、然后在其他节点上执行rabbitmqctl forget_cluster_node rabbit@node2 命令将 node2 节点剔除出去.
		3、这种方式适用于node2不在需要运行的情况。

*	方式二：
		1、在 node2 上执行 rabbitmqctl reset 命令
		该命令也会和集群中的磁盘节点进行通信 告诉它们该节点正在离开集群。不然集群会认为该节点出了故障 并期望其最终能够恢复过来。
	

# 流控
	流控机制是用来避免消息的发送速率过快从而导致服务器难以支撑的情形。
