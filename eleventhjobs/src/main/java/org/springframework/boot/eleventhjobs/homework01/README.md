# homework01

在win10 安装了Ubuntu 子系统. Ubuntu 装了redis服务器. redis版本为6.2.4 64bit. redis的相关命令配置为环境变量. 

启动前需要编辑/etc/sysctl.conf ，设置vm.overcommit_memory=1，然后sysctl -p 使配置文件生效

## redis主从复制

- 配置文件. 配置信息注释在配置文件中
    - redis6379.conf 
    - redis6380.conf 
    - 配置文件的相关说明在redis6380.conf 中
- 启动
    - `redis-server redis6379.conf` 启动端口为6379的主服务器, 用于读写
    - `redis-server redis6380.conf` 启动端口为6380的从服务器, 只读
- 验证
    1. `redis-cli -p 6380` 客户端连接从服务器后, 输入`set ck "in JAC"` 后收到`(error) READONLY You can't write against a read only replica.` 消息, 说明从服务器不能写.
    2. redis6379.conf 中取消`replicaof 127.0.0.1 6380`. 启动6379, 6380服务器后, 6379服务器调用`set ck 6379`. 6380服务器调用`set ck 6380` 6380服务器执行`get ck` 返回`6380`. 6380服务器调用`slaveof 127.0.0.1 6379` 后, 再执行`get ck` 返回结果为`6379`. 说明设置了主从关系后, 从服务器复制主服务器数据数据. 执行slaveof 命令后, 主服务器写出日志. 记录了主服务器复制相关信息
 ```log
11766:M 13 Jul 2021 20:46:23.191 * Replica 127.0.0.1:6380 asks for synchronization
11766:M 13 Jul 2021 20:46:23.191 * Partial resynchronization not accepted: Replication ID mismatch (Replica asked for 'f8fd46426a787504449eadabd444152271e4f3b4', my replication IDs are '03a2b1bdf0b7927eeb3bb5ea178466f937eb3b97' and '0000000000000000000000000000000000000000')
11766:M 13 Jul 2021 20:46:23.191 * Replication backlog created, my new replication IDs are '9c4392ebf79e8fa980b680127ac5b67c30d94065' and '0000000000000000000000000000000000000000'
11766:M 13 Jul 2021 20:46:23.191 * Starting BGSAVE for SYNC with target: disk
11766:M 13 Jul 2021 20:46:23.205 * Background saving started by pid 11779
11779:C 13 Jul 2021 20:46:23.215 * DB saved on disk
11779:C 13 Jul 2021 20:46:23.215 * RDB: 2 MB of memory used by copy-on-write
11766:M 13 Jul 2021 20:46:23.264 * Background saving terminated with success
11766:M 13 Jul 2021 20:46:23.264 * Synchronization with replica 127.0.0.1:6380 succeeded
```
从服务器写出日志. 记录了从服务器复制相关信息
```log
11772:S 13 Jul 2021 20:46:23.190 * Before turning into a replica, using my own master parameters to synthesize a cached master: I may be able to synchronize with the new master with just a partial transfer.
11772:S 13 Jul 2021 20:46:23.190 * Connecting to MASTER 127.0.0.1:6379
11772:S 13 Jul 2021 20:46:23.190 * MASTER <-> REPLICA sync started
11772:S 13 Jul 2021 20:46:23.190 * REPLICAOF 127.0.0.1:6379 enabled (user request from 'id=3 addr=127.0.0.1:58584 laddr=127.0.0.1:6380 fd=8 name= age=347 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=42 qbuf-free=40912 argv-mem=20 obl=0 oll=0 omem=0 tot-mem=61484 events=r cmd=slaveof user=default redir=-1')
11772:S 13 Jul 2021 20:46:23.190 * Non blocking connect for SYNC fired the event.
11772:S 13 Jul 2021 20:46:23.190 * Master replied to PING, replication can continue...
11772:S 13 Jul 2021 20:46:23.191 * Trying a partial resynchronization (request f8fd46426a787504449eadabd444152271e4f3b4:1).
11772:S 13 Jul 2021 20:46:23.206 * Full resync from master: 9c4392ebf79e8fa980b680127ac5b67c30d94065:0
11772:S 13 Jul 2021 20:46:23.206 * Discarding previously cached master state.
11772:S 13 Jul 2021 20:46:23.264 * MASTER <-> REPLICA sync: receiving 187 bytes from master to disk
11772:S 13 Jul 2021 20:46:23.265 * MASTER <-> REPLICA sync: Flushing old data
11772:S 13 Jul 2021 20:46:23.265 * MASTER <-> REPLICA sync: Loading DB in memory
11772:S 13 Jul 2021 20:46:23.271 * Loading RDB produced by version 6.2.4
11772:S 13 Jul 2021 20:46:23.271 * RDB age 0 seconds
11772:S 13 Jul 2021 20:46:23.271 * RDB memory usage when created 1.85 Mb
11772:S 13 Jul 2021 20:46:23.272 * MASTER <-> REPLICA sync: Finished with success
```

## sentinel 高可用

- 配置文件
    - sentinel26379.conf. 
    - sentinel26380.conf.
    - 配置说明在sentinel26380.conf 中.
- 启动命令
    - 启动redis主, 从服务器.
    - 启动哨兵,`redis-sentinel sentinel26379.conf`
    配置接口为26379, 监视接口6379 的主服务器
    - 启动哨兵,``redis-sentinel sentinel26380.conf`
    配置接口为26380, 监视接口6379 的主服务器
- 验证
    - 主服务器关闭后. 
        -
```log
#  端口为26379的哨兵写日志.  记录从主服务器宕机到自动故障迁移
11792:X 13 Jul 2021 20:57:01.636 # +sdown sentinel 8d992c54df8f8677b0b345825f61fb733c73d14d 127.0.0.1 26380 @ mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:57:01.636 * +convert-to-slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:57:05.228 # -sdown sentinel 8d992c54df8f8677b0b345825f61fb733c73d14d 127.0.0.1 26380 @ mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:37.066 # +sdown master mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:37.128 # +odown master mymaster 127.0.0.1 6379 #quorum 2/2
11792:X 13 Jul 2021 20:58:37.128 # +new-epoch 1
11792:X 13 Jul 2021 20:58:37.128 # +try-failover master mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:37.150 # +vote-for-leader 8d992c54df8f8677b0b345825f61fb733c73d14c 1
11792:X 13 Jul 2021 20:58:37.170 # 8d992c54df8f8677b0b345825f61fb733c73d14d voted for 8d992c54df8f8677b0b345825f61fb733c73d14c 1
11792:X 13 Jul 2021 20:58:37.241 # +elected-leader master mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:37.241 # +failover-state-select-slave master mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:37.332 # +selected-slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:37.332 * +failover-state-send-slaveof-noone slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:37.409 * +failover-state-wait-promotion slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:38.375 # +promoted-slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:38.375 # +failover-state-reconf-slaves master mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:38.406 # +failover-end master mymaster 127.0.0.1 6379
11792:X 13 Jul 2021 20:58:38.406 # +switch-master mymaster 127.0.0.1 6379 127.0.0.1 6380
11792:X 13 Jul 2021 20:58:38.406 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
11792:X 13 Jul 2021 20:58:48.450 # +sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380

#  哨兵服务器26370 写出日志. 记录主服务器宕机, 发起选主服务器投票, 自动故障迁移成功
11799:X 13 Jul 2021 20:58:37.058 # +sdown master mymaster 127.0.0.1 6379
11799:X 13 Jul 2021 20:58:37.160 # +new-epoch 1
11799:X 13 Jul 2021 20:58:37.169 # +vote-for-leader 8d992c54df8f8677b0b345825f61fb733c73d14c 1
11799:X 13 Jul 2021 20:58:38.238 # +odown master mymaster 127.0.0.1 6379 #quorum 2/2
11799:X 13 Jul 2021 20:58:38.238 # Next failover delay: I will not start a failover before Tue Jul 13 21:04:37 2021
11799:X 13 Jul 2021 20:58:38.416 # +config-update-from sentinel 8d992c54df8f8677b0b345825f61fb733c73d14c 127.0.0.1 26379 @ mymaster 127.0.0.1 6379
11799:X 13 Jul 2021 20:58:38.416 # +switch-master mymaster 127.0.0.1 6379 127.0.0.1 6380
11799:X 13 Jul 2021 20:58:38.416 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
11799:X 13 Jul 2021 20:58:48.458 # +sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380

# 从服务器日志, 记录主服务器宕机, 整个投票过程, 自动故障迁移成功
11787:S 13 Jul 2021 20:57:01.637 * Before turning into a replica, using my own master parameters to synthesize a cached master: I may be able to synchronize with the new master with just a partial transfer.
11787:S 13 Jul 2021 20:57:01.637 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:57:01.637 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:57:01.637 * REPLICAOF 127.0.0.1:6379 enabled (user request from 'id=3 addr=127.0.0.1:58606 laddr=127.0.0.1:6380 fd=8 name=sentinel-8d992c54-cmd age=10 idle=0 flags=x db=0 sub=0 psub=0 multi=4 qbuf=196 qbuf-free=40758 argv-mem=4 obl=45 oll=0 omem=0 tot-mem=61468 events=r cmd=exec user=default redir=-1')
11787:S 13 Jul 2021 20:57:01.649 # CONFIG REWRITE executed with success.
11787:S 13 Jul 2021 20:57:01.649 * Non blocking connect for SYNC fired the event.
11787:S 13 Jul 2021 20:57:01.649 * Master replied to PING, replication can continue...
11787:S 13 Jul 2021 20:57:01.650 * Trying a partial resynchronization (request 5df51d791abef3c51249d1cba148f2f90a5e5b78:1).
11787:S 13 Jul 2021 20:57:01.652 * Full resync from master: 437631af31c4d56c0cad132db01d02115af2168a:0
11787:S 13 Jul 2021 20:57:01.652 * Discarding previously cached master state.
11787:S 13 Jul 2021 20:57:01.756 * MASTER <-> REPLICA sync: receiving 187 bytes from master to disk
11787:S 13 Jul 2021 20:57:01.756 * MASTER <-> REPLICA sync: Flushing old data
11787:S 13 Jul 2021 20:57:01.756 * MASTER <-> REPLICA sync: Loading DB in memory
11787:S 13 Jul 2021 20:57:01.771 * Loading RDB produced by version 6.2.4
11787:S 13 Jul 2021 20:57:01.771 * RDB age 0 seconds
11787:S 13 Jul 2021 20:57:01.771 * RDB memory usage when created 1.91 Mb
11787:S 13 Jul 2021 20:57:01.771 * MASTER <-> REPLICA sync: Finished with success
11787:S 13 Jul 2021 20:58:26.973 # Connection with master lost.
11787:S 13 Jul 2021 20:58:26.973 * Caching the disconnected master state.
11787:S 13 Jul 2021 20:58:26.973 * Reconnecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:26.973 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:26.973 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:27.043 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:27.043 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:27.043 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:28.049 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:28.049 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:28.049 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:29.054 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:29.054 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:29.055 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:30.059 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:30.060 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:30.060 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:31.064 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:31.064 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:31.064 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:32.069 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:32.069 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:32.069 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:33.074 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:33.074 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:33.074 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:34.080 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:34.080 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:34.080 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:35.085 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:35.085 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:35.085 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:36.090 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:36.090 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:36.090 # Error condition on socket for SYNC: Connection refused
11787:S 13 Jul 2021 20:58:37.094 * Connecting to MASTER 127.0.0.1:6379
11787:S 13 Jul 2021 20:58:37.094 * MASTER <-> REPLICA sync started
11787:S 13 Jul 2021 20:58:37.095 # Error condition on socket for SYNC: Connection refused
11787:M 13 Jul 2021 20:58:37.409 * Discarding previously cached master state.
11787:M 13 Jul 2021 20:58:37.409 # Setting secondary replication ID to 437631af31c4d56c0cad132db01d02115af2168a, valid up to offset: 11056. New replication ID is b30f68596988ba2fde52a4869aac55125a01172a
11787:M 13 Jul 2021 20:58:37.409 * MASTER MODE enabled (user request from 'id=3 addr=127.0.0.1:58606 laddr=127.0.0.1:6380 fd=8 name=sentinel-8d992c54-cmd age=106 idle=0 flags=x db=0 sub=0 psub=0 multi=4 qbuf=188 qbuf-free=40766 argv-mem=4 obl=45 oll=0 omem=0 tot-mem=61468 events=r cmd=exec user=default redir=-1')
11787:M 13 Jul 2021 20:58:37.425 # CONFIG REWRITE executed with success.
```        
    
## Cluster集群

- 配置文件
    - cluster6379.conf, cluster6380.conf, cluster6381.conf, cluster6382.conf, cluster6383.conf, cluster6384.conf
    - 每个文件放入对应编号的文件夹中,文件夹名前加'rc'.  例如`cluster6379.conf` 放入`rc6379`文件夹中.
    - 每个配置文件后面的编号都是设置的端口. 例如`cluster6379.conf` 设置的端口为6379.
    - 配置文件相关说明在cluster6384.conf 中
  
- 启动命令
    - 在每个文件夹中调用命令`redis-server 配置文件`. 例如`redis-server cluster6379.conf` 这样服务器启动后, 每个redis 服务器的`appendonly.aof`,`dump.rdb`, `nodes.conf`会放置到对应的文件夹中.
    - `redis-cli --cluster create  127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 --cluster-replicas 1` 启动redis 集群. 并且`cluster-replicas 1` 设定每个主服务器有一个从服务器. 日志中记录有主从对应关系, 以及每个主服务器分配的槽.
```log
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 127.0.0.1:6383 to 127.0.0.1:6379
Adding replica 127.0.0.1:6384 to 127.0.0.1:6380
Adding replica 127.0.0.1:6382 to 127.0.0.1:6381
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: f1aa5a8eba582b5c48aef256f284b2ed379b12b7 127.0.0.1:6379
   slots:[0-5460] (5461 slots) master
M: de135554b29e2a206f20c7e1f3ff63a592a2da01 127.0.0.1:6380
   slots:[5461-10922] (5462 slots) master
M: 15169378dda6013dd4da2d2a101f96e027337d72 127.0.0.1:6381
   slots:[10923-16383] (5461 slots) master
S: e99b1dddcfe6e02b0ef2f7c0987bb8f290e5ed99 127.0.0.1:6382
   replicates 15169378dda6013dd4da2d2a101f96e027337d72
S: 941682d6089d851a839cc13f7ab8d7bca68238e1 127.0.0.1:6383
   replicates f1aa5a8eba582b5c48aef256f284b2ed379b12b7
S: 7d644c4b80ffea5ab8327a00c9a96a61b5e08b6b 127.0.0.1:6384
   replicates de135554b29e2a206f20c7e1f3ff63a592a2da01
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
.
>>> Performing Cluster Check (using node 127.0.0.1:6379)
M: f1aa5a8eba582b5c48aef256f284b2ed379b12b7 127.0.0.1:6379
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
M: 15169378dda6013dd4da2d2a101f96e027337d72 127.0.0.1:6381
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
S: 7d644c4b80ffea5ab8327a00c9a96a61b5e08b6b 127.0.0.1:6384
   slots: (0 slots) slave
   replicates de135554b29e2a206f20c7e1f3ff63a592a2da01
M: de135554b29e2a206f20c7e1f3ff63a592a2da01 127.0.0.1:6380
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: 941682d6089d851a839cc13f7ab8d7bca68238e1 127.0.0.1:6383
   slots: (0 slots) slave
   replicates f1aa5a8eba582b5c48aef256f284b2ed379b12b7
S: e99b1dddcfe6e02b0ef2f7c0987bb8f290e5ed99 127.0.0.1:6382
   slots: (0 slots) slave
   replicates 15169378dda6013dd4da2d2a101f96e027337d72
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```    

- 验证 
   - `redis-cli -c -p 6379` 开启redis 集群. 
   - `set zhangsan ssssssssssssss` 返回信息为`-> Redirected to slot [12767] located at 127.0.0.1:6381 ` 说明将`zhangsan` 存储到了6381服务器的12767槽中
   - `set wangwu wwwwwwwwwwwwwwwwwwwww` 返回信息为`-> Redirected to slot [9439] located at 127.0.0.1:6380` 说明将`wangwu` 存储到了6380服务器的9439槽中.
