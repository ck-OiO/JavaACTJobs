# shardingshpere 中创建数据库和表
create database shopping default character set utf8mb4;
CREATE TABLE `t_order` (
	`f_order_id` BIGINT NOT NULL AUTO_INCREMENT,
	`f_user_id` INT NULL,
	`f_create_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`f_total_value` DECIMAL (10, 5) NULL COMMENT '该订单总额度',
	`f_address` VARCHAR (255) NULL COMMENT '订单地址',
	`f_status` TINYINT NULL DEFAULT 1 COMMENT '订单状态: 0:不可用  1:未支付  2:已支付 3:已发货 4:已到达 5:已签收',
	PRIMARY KEY (`f_order_id`)
# 表不能设置默认存储引擎
)  DEFAULT CHARACTER SET = utf8mb4 COMMENT = '订单信息';


# 在mysql 01 中创建数据库
create database shopping00 default character set utf8mb4;


# 在mysql 02 中创建数据库
create database shopping01 default character set utf8mb4;

