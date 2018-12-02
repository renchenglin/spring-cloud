# docker 安装mongodb

docker pull mongo:latest

docker-compose -f ./docker-compose.yml up

docker ps 查看docker创建的mongo的容器名字
使用docker exec -it <mongo的容器名字mongo_mongodb_1> mongo admin 进入容器

创建 userAdminAnyDatabase和root角色的用户admin和root
use admin
db.createUser({ user: 'admin', pwd: 'admin', roles: [{role: 'userAdminAnyDatabase', db: 'admin'}]});

db.createUser({ user: 'root', pwd: 'root', roles: [{role: 'root', db: 'admin'}]});

```
[root@localhost ~]# docker exec -it mongo_mongodb_1 mongo admin
MongoDB shell version v4.0.4
connecting to: mongodb://127.0.0.1:27017/admin
Implicit session: session { "id" : UUID("1b602c6d-e843-417f-803d-4d4aad5b7185") }
MongoDB server version: 4.0.4
> db.auth("admin","admin")
1
```
# 数据库和集合管理（创建、查看、删除）

## 创建database
use my_database_name
MongoDB返回以下信息，use 命令只是向MongoDB注册database，并没有实际的创建使用show dbs 查看，列表中没有该database。
db.my_database_name.insert({_id:1,name:"test"})  
此时，MongoDB真正创建database，查看存储数据的folder，发现多了两个.wt文件，一个用于存储数据，一个用于存储index。使用show dbs 查看，列表中存在该database

## 删除database
删除database时，必须十分小心，除非用于测试环境，否则，不要轻易使用这个命令

1，使用use命令，切换到指定的database

use database_name
2，使用db命令，查看当前database，避免删错

db
3，删除当前database

db.dropDatabase()

## 创建集合

*	隐式创建Collection：	db.foo.insert({_id:1,name:"test"})
	在MongoDB中，Collection相当于关系型数据库的Table，用户不需要显式定义Collection就能向Collection插入数据。
	在第一次向Collection插入数据时，MongoDB会自动创建Collection；
	如果Collection已经存在于Database中，那么MongoDB直接向Collection中插入数据。
	
*	显式创建Collection：	db.createCollection("log", { capped : true, size : 5242880, max : 5000 } )
	
## 删除集合
	db.collection_name.drop()

# mongodb权限体系

## 创建用户
db.createUser({user:"usertest",pwd:"passtest",
	roles:[  
		{role:"clusterAdmin", db:"admin" }, 
		{role:"readAnyDatabase",db:"admin" }, 
		{role:"readWrite",db:"testDB" } ]});
		
db.createUser({user:"usertest",pwd:"passtest",roles:[ {role:"dbAdmin", db:"test" }, {role:"dbOwner",db:"test" }]});

## mongodb用户角色

*	1.数据库用户角色：read、readWrite;  
*	2.数据库管理角色：dbAdmin、dbOwner、userAdmin；       
*	3.集群管理角色：clusterAdmin、clusterManager、clusterMonitor、hostManager；
*	4.备份恢复角色：backup、restore；
*	5.所有数据库角色：readAnyDatabase、readWriteAnyDatabase、userAdminAnyDatabase、dbAdminAnyDatabase
*	6.超级用户角色：root  
*	7.内部角色：__system

角色说明：

Read：                             允许用户读取指定数据库

readWrite：                     允许用户读写指定数据库

dbAdmin：                      允许用户在指定数据库中执行管理函数，如索引创建、删除，查看统计或访问system.profile

userAdmin：                    允许用户向system.users集合写入，可以找指定数据库里创建、删除和管理用户

dbOwner：                       允许在当前DB中执行任意操作

readAnyDatabase：          赋予用户所有数据库的读权限，只在admin数据库中可用

readWriteAnyDatabase： 赋予用户所有数据库的读写权限，只在admin数据库中可用

userAdminAnyDatabase：赋予用户所有数据库管理User的权限，只在admin数据库中可用

dbAdminAnyDatabase：   赋予管理所有数据库的权限，只在admin数据库中可用

root：                                 超级账号，超级权限，只在admin数据库中可用。

------------------------------------------------------------------------------------------

集群管理角色：

clusterAdmin：                  赋予管理集群的最高权限，只在admin数据库中可用

clusterManager：               赋予管理和监控集群的权限

clusterMonitor：                赋予监控集群的权限，对监控工具具有readonly的权限

hostManager：                   赋予管理Server

## 修改用户密码

*	方法1：db.changeUserPassword("usertest","changepass");

*	方法2：db.updateUser("usertest",{pwd:"changepass1"})；

## 修改用户权限

db.updateUser("usertest",{roles:[ {role:"read",db:"testDB"} ]})

注：updateuser它是完全替换之前的值，如果要新增或添加roles而不是代替它 则使用方法： db.grantRolesToUser() 和 db.revokeRolesFromUser(）

------------------------------------------------------------------------------------------
修改权限：db.grantRolesToUser("usertest", [{role:"readWrite", db:"testDB"},{role:"read", db:"testDB"}])  

删除权限：db.revokeRolesFromUser("usertest",[{role:"read", db:"testDB"}])   

## 删除用户

db.dropUser('usertest')

# 索引

## 索引管理

*	如果没有索引，查询是也没有加limit限制返回的结果，就会进行全表扫描
*	索引的效果是根据查询条件，排序方式以及真实的数据决定的
*	如果查询的结果集大于32M，mongodb就会报错，拒绝为如此多的数据进行排序

### 创建索引

*	使用ensureIndex函数创建索引：如db.collection.ensureIndex({"username":1})
*	每个集合最多64个索引
*	创建的索引存储在system.indexes集合中，可以在该集合中看到索引的元数据。system.indexes是一个保留集合，不能直接插入、删除文档，只能通过ensureIndex、dropIndex的方式录入索引元数据信息
*	查看索引信息：db.collectionName.getIndexes()
*	索引元数据信息中，key、name最重要。key
*	可以对内部文档和内部文档key进行索引
*	可以对数组进行索引

### 唯一索引与稀疏索引

*	唯一索引可以确保集合的每个文档的指定键都有唯一值
*	建立唯一索引的key，只允许一个没有key字段的文档插入集合，没有key的当做null进行索引
*	稀疏索引是只有key存在时才生效：db.person.ensureIndex({"mobilephone":1}{"unique":true,"sparse":true});
*	稀疏索引不必是唯一的，只要去掉unique选项，就可以创建一个非唯一的稀疏索引

### 删除索引

*	随着数据的变化，以前的索引不在高效，需要删除索引，重新建立索引
* 	使用dropIndex: 如db.person.dropIndex({"age":1})
*	使用background选项

### 低效索引操作符

*	$where无法使用索引
*	$ne可以使用索引，但是效率不高
*	$exist
*	$not
*	$nin总是全表扫描

## Replica Set

使用docker部署Replica Set

docker run --name mongo-0 -v /root/mongo/db0:/data/db -p 27017:27017 -d mongo mongod --replSet rs0 --port 27017 --bind_ip 0.0.0.0 --dbpath /data/db --smallfiles --oplogSize 128  
docker run --name mongo-1 -v /root/mongo/db1:/data/db -p 27018:27017 -d mongo mongod --replSet rs0 --port 27017 --bind_ip 0.0.0.0 --dbpath /data/db --smallfiles --oplogSize 128  
docker run --name mongo-2 -v /root/mongo/db2:/data/db -p 27019:27017 -d mongo mongod --replSet rs0 --port 27017 --bind_ip 0.0.0.0 --dbpath /data/db --smallfiles --oplogSize 128  

查看docker容器的ip
docker inspect mongo-0 | grep IPAddress 

进入docker容器，使用mongo连接到admin数据库
docker exec -it mongo-0 mongo admin

执行 Replica Set 初始化脚本
rs.initiate( {
    _id : "rs0",
    members: [
       { _id: 0, host: "172.17.0.2:27017" },
       { _id: 1, host: "172.17.0.3:27017" },
       { _id: 2, host: "172.17.0.4:27017" }
    ]
 })

启动一个测试mongo 连接到 Replica Set
docker run --name client-rs -it mongo mongo mongodb://172.17.0.2:27017,172.17.0.3:27017,172.17.0.4:27017/rs-test?replicaSet=rs0

进行测试
需要注意的是在事务里面不能创建collection，所以提前建好一个：
rs0:PRIMARY> use rs-test
switched to db rs-test
rs0:PRIMARY> db.book.insert({"name":"docker in action"})
WriteResult({ "nInserted" : 1 })
rs0:PRIMARY> db.book.find()
{ "_id" : ObjectId("5b91452af5a0af9da451f044"), "name" : "docker in action" }


开启事务，插入数据并回滚
rs0:PRIMARY> db.book.find()
{ "_id" : ObjectId("5b91452af5a0af9da451f044"), "name" : "docker in action" }
rs0:PRIMARY> session = db.getMongo().startSession()
session { "id" : UUID("5720db1a-b16a-4868-afb3-6658a3ac75fa") }
rs0:PRIMARY> db = session.getDatabase('rs-test')
rs-test
rs0:PRIMARY> session.startTransaction()
rs0:PRIMARY> db.book.insert({"name":"java in action"})
WriteResult({ "nInserted" : 1 })
rs0:PRIMARY> db.book.find()
{ "_id" : ObjectId("5b91452af5a0af9da451f044"), "name" : "docker in action" }
{ "_id" : ObjectId("5b914751f5a0af9da451f047"), "name" : "java in action" }
rs0:PRIMARY> session.abortTransaction()
rs0:PRIMARY> db.book.find()
{ "_id" : ObjectId("5b91452af5a0af9da451f044"), "name" : "docker in action" }


开启认证
openssl rand -base64 741 > mongodb-keyfile

此处有一个坑，虚拟机中创建mongodb-keyfile是使用root账户创建，文件挂载到容器中，文件的用户是root，容器中mongod进程是mongodb用户启动的，无论怎么调整权限都不行
使用不开启认证的方式启动一个容器，进入看文件权限发现db目录的所属用户是mongodb,而挂载盘中的所属的用户竟然是polkitd,调整所属到polkitd就可以了，折腾了两
chmod 600 mongodb-keyfile
chown polkitd:root mongodb-keyfile

docker run --name mongo-0 -v /root/mongo/db0:/data/db -v /root/mongo/mongodb-keyfile:/data/mongodb-keyfile -p 27017:27017 -d mongo    mongod --replSet rs0 --port 27017 --bind_ip 0.0.0.0 --dbpath /data/db --smallfiles --oplogSize 128 --keyFile /data/mongodb-keyfile  --auth       
docker run --name mongo-1 -v /root/mongo/db1:/data/db -v /root/mongo/mongodb-keyfile:/data/mongodb-keyfile -p 27018:27017 -d mongo    mongod --replSet rs0 --port 27017 --bind_ip 0.0.0.0 --dbpath /data/db --smallfiles --oplogSize 128 --keyFile /data/mongodb-keyfile  --auth    
docker run --name mongo-2 -v /root/mongo/db2:/data/db -v /root/mongo/mongodb-keyfile:/data/mongodb-keyfile -p 27019:27017 -d mongo    mongod --replSet rs0 --port 27017 --bind_ip 0.0.0.0 --dbpath /data/db --smallfiles --oplogSize 128 --keyFile /data/mongodb-keyfile  --auth     

进入docker容器，使用mongo连接到admin数据库
docker exec -it mongo-0 mongo admin

rs.initiate( {
    _id : "rs0",
    members: [
       { _id: 0, host: "172.17.0.2:27017" },
       { _id: 1, host: "172.17.0.3:27017" },
       { _id: 2, host: "172.17.0.4:27017" }
    ]
 })
 
 并确保在当前节点是primary节点时，创建用户root用户
use admin
db.createUser({ user: 'root', pwd: 'root', roles: [{role: 'root', db: 'admin'}]});
db.auth("root","root");
db.createUser({user:"usertest",pwd:"passtest",roles:[ {role:"dbAdmin", db:"rs-test" }, {role:"dbOwner",db:"rs-test" }]});

另开一个容器，连接到副本集中，并进行用户身份认证
docker run --name client-rs -it mongo mongo mongodb://172.17.0.2:27017,172.17.0.3:27017,172.17.0.4:27017/rs-test?replicaSet=rs0
db.auth("usertest","passtest")

测试事务见上面的步骤

当前还有一个问题：就是使用robo 3t使用Replica Set的方式无法连接到集群，不知道是网络原因还是啥
docker的端口映射如下
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                      NAMES
4d64af97c0c8        mongo               "docker-entrypoint.s…"   18 minutes ago      Up 18 minutes       27017/tcp                  client-rs
9be55ec16b97        mongo               "docker-entrypoint.s…"   32 minutes ago      Up 32 minutes       0.0.0.0:27019->27017/tcp   mongo-2
12a8b17d5c19        mongo               "docker-entrypoint.s…"   32 minutes ago      Up 32 minutes       0.0.0.0:27018->27017/tcp   mongo-1
9b0e36e1c464        mongo               "docker-entrypoint.s…"   34 minutes ago      Up 34 minutes       0.0.0.0:27017->27017/tcp   mongo-0


参考：
https://blog.csdn.net/u010010606/article/details/79701772
https://www.linuxidc.com/Linux/2017-03/142379.htm
https://docs.mongodb.com/manual/tutorial/enforce-keyfile-access-control-in-existing-replica-set/
https://blog.csdn.net/wisfy_21/article/details/82470149

