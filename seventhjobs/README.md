作业02, 09, 10 共用了一个Order和OrderMapper

## homework02:
- 需要单个数据源
- 将application.yml 的spring.profiles.active 设置为 homework02
- com.github.ck_oio.javaatcjobs.seventjobs.homework02.Homework02OrderService 使用每条记录一个事务, 所有的insert 使用一个事务, 批量插入 三种方式进行了插入. rewriteBatchedStatements 是否设置对mybatis没有影响.
- com.github.ck_oio.javaatcjobs.seventjobs.homework02.Homework02OrderServiceTest 对每个方法进行了测试, 输出插入时间的结果. 

## homework09:
- 需要使用两个数据源, 一个写入, 一个读取. 且两个数据源是主从复制. 
- 将application.yml 的spring.profiles.active 设置为 homework09
- com.github.ck_oio.javaatcjobs.seventjobs.homework09.Homework09OrderService 使用没有注解, 注解使用写数据源, 注释使用读数据源三个条件编写方法.
- com.github.ck_oio.javaatcjobs.seventjobs.homework09.Homework09OrderServiceTest 对每个方法进行了测试, 输出日志显示数据源名称. 测试结果放在resource/homework09/routingDataSource.log.

## homework10:
- 需要两个数据源, 并且两个数据源都可以读写. 使用ShardingSphere 进行了代理. 持久层只设置了代理数据源信息.
- 将application.yml 的spring.profiles.active 设置为 homework10
- resorces/homework10 中保存了shardingShpere的 server.yaml和config-sharding.yaml 的设置. init.sql 是代理数据库和实际数据的添加数据库和表的sql 语句
- com.github.ck_oio.javaatcjobs.seventjobs.homework02.Homework02OrderService 测试有主键的数据, 没有主键的数据, 从代理数据源中获取所有数据, 代理数据源的日志会显示调用了哪个数据源以及sql语句, 测试结果放在resource/homework10/shardingShpere.log 中
- com.github.ck_oio.javaatcjobs.seventjobs.homework02.Homework02OrderServiceTest 对每个方法进行了测试, 测试结果保存在resource/homework10/shardingShpere.log. 


