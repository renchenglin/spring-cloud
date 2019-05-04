# 给mysql表设置自增主键

set sql_mode='';
set sql_mode='NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES';
alter table t_user modify id bigint(20)  auto_increment;
alter table t_user  auto_increment=10000; 

