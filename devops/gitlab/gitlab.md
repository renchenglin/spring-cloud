# docker安装gitlab

docker pull gitlab/gitlab-ce:11.5.0-ce.0

docker-compose -f ./docker-compose.yml up

启动完成后，访问http://192.168.31.105/
设置登录密码，默认的管理员账户为root 

找回管理员密码，参考：https://www.jianshu.com/p/25afcfd02019

