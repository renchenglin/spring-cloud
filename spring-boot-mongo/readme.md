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
