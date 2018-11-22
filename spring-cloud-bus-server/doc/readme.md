# 安装

```

docker pull rabbitmq:3.7.8-management
docker run -d --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3.7.8-management

运行容器
docker run -d  -p 5671:5671 -p 5672:5672  -p 15672:15672 -p 15671:15671  -p 25672:25672  -v /root/data/rabbitmq-data/:/var/rabbitmq/lib  --name rabbitmq rabbitmq:3.7.8-management

访问管理端
http://192.168.31.136:15672

进入容器
docker exec -it rabbitmq /bin/bash

```

