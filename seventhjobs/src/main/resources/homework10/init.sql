# shardingShpere(简称ss) 中相关配置以及sql语句

## 执行顺序

1. 在两个实际DBMS中创建shopping .
2. ss 的config-sharding.yaml配置逻辑数据库名shopping. 以及ss 中数据库, 表和实际数据库, 表的关系
3. 配置ss 中其他内容
4. 在ss 的数据库连接中执行`t_order`的创建语句. 实际数据库中也会按照ss中配置创建数据.

## 相关sql 语句

```sql
-- shardingshpere 中创建数据库和表
create database shopping default character set utf8mb4;
CREATE TABLE `t_order` (
	`f_order_id` BIGINT NOT NULL AUTO_INCREMENT,
	`f_user_id` BIGINT NULL,
	`f_create_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`f_total_value` DECIMAL (10, 5) NULL COMMENT '该订单总额度',
	`f_address` VARCHAR (255) NULL COMMENT '订单地址',
	`f_status` TINYINT NULL DEFAULT 1 COMMENT '订单状态: 0:不可用  1:未支付  2:已支付 3:已发货 4:已到达 5:已签收',
	PRIMARY KEY (`f_order_id`)
-- 表不能设置默认存储引擎
)  DEFAULT CHARACTER SET = utf8mb4 COMMENT = '订单信息';

-- 在mysql 01 中创建数据库
create database shopping00 default character set utf8mb4;

-- 在mysql 02 中创建数据库
create database shopping01 default character set utf8mb4;
```
