docker pull mongo:latest

docker-compose -f ./docker-compose.yml up

use admin
db.createUser({ user: 'admin', pwd: 'admin', roles: [{role: 'userAdminAnyDatabase', db: 'admin'}]});

```
[root@localhost ~]# docker exec -it mongo_mongodb_1 mongo admin
MongoDB shell version v4.0.4
connecting to: mongodb://127.0.0.1:27017/admin
Implicit session: session { "id" : UUID("1b602c6d-e843-417f-803d-4d4aad5b7185") }
MongoDB server version: 4.0.4
> db.auth("admin","admin")
1
```

