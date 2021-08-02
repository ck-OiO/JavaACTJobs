# kafka集群启动和测试

## 启动kafka集群

win10 系统下, jdk 版本为11, 文件编码格式为utf-8. 操作的默认地址为kafka的文件夹下. 配置文件为当前文件夹下.

1. 启动zookeeper, 管理kafka集群.
`bin\windows\zookeeper-server-start.bat config\zookeeper.properties` 

2. 启动三个kafka 代理. 配件文件的键都相同. 说明在`kafka9001.properties`中

`bin\windows\kafka-server-start.bat kafka9001.properties`
`bin\windows\kafka-server-start.bat kafka9002.properties`
`bin\windows\kafka-server-start.bat kafka9003.properties`

## 测试klafka集群

1. 创建副本因子为2, 分区为3, 名称为test32 的topic. 分区和副本因子是根据kafka服务器的个数确定.`bin\windows\kafka-topics.bat --zookeeper localhost:2181 --create --topic test32 --partitions 3 --replication-factor 2`
2. 显示所有topic:`bin\windows\kafka-topics.bat --zookeeper localhost:2181 --list`. 显示结果为`test32`
3. 查看test32的所有版本信息:`bin\windows\kafka-topics.bat --zookeeper localhost:2181 --describe --topic test32`. 结果:
```log
Topic: test32   PartitionCount: 3       ReplicationFactor: 2    Configs:
# 每个kafka 服务器代理一个可读写的主分区了. "1,2" 表示主分区0有两个副本. Isr:表示可主副本数据一致的从副本的id.
        Topic: test32   Partition: 0    Leader: 1       Replicas: 1,2   Isr: 1,2
        Topic: test32   Partition: 1    Leader: 2       Replicas: 2,3   Isr: 2,3
        Topic: test32   Partition: 2    Leader: 3       Replicas: 3,1   Isr: 3,1
```
4. 生产者性能测试:
```log
# 数据数量为100000, 数据大小为1000Byte, 每秒最多200000数据.
bin\windows\kafka-producer-perf-test.bat --topic test32 --num-records 100000 --record-size 1000 --throughput 200000 --producer-props bootstrap.servers=localhost:9002
# 测试结果: 
# 56897条数据发送时的每秒发送量, 平均延迟, 最大延迟
56897 records sent, 11379.4 records/sec (10.85 MB/sec), 1226.3 ms avg latency, 2751.0 ms max latency.
# 100000 条数据发送时的每秒发送数据数量, 平均延迟, 最大延迟, 发送50%的耗时为1729ms, 95%耗时2680ms, 99%耗时2780ms, 99.9%耗时2801ms, 
100000 records sent, 13659.336156 records/sec (13.03 MB/sec), 1611.80 ms avg latency, 2809.00 ms max latency, 1729 ms 50th, 2680 ms 95th, 2780 ms 99th, 2801 ms 99.9th.
```

`bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9001 --topic test32 --from-beginning`
5. `test32`消费者性能测试:
```log
# 消费者测试命令: 使用单线程测试
bin\windows\kafka-consumer-perf-test.bat --bootstrap-server localhost:9002 --topic test32 --fetch-size 1048576 --messages 100000 --threads 1
# 测试结果
# 开始时间, 结束时间, 每MB数据消耗的时间, 每秒消费的数据量
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2021-07-25 10:54:30:294, 2021-07-25 10:54:31:641, 95.6803, 71.0321, 100330, 74484.0386, 1627181671079, -1627181669732, -0.0000, -0.0001
```
