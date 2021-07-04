-- 创建bank01数据库和account_info表
CREATE DATABASE IF NOT EXISTS `bank01` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

USE `bank01`;

CREATE TABLE `account_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `acount_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '户主姓名',
  `balance_cny` double DEFAULT NULL COMMENT '人民币帐户余额',
  `balance_usd` double DEFAULT NULL COMMENT '美元帐户余额',
  `frozen_cny` double DEFAULT NULL COMMENT '人民币冻结金额',
  `frozen_usd` double DEFAULT NULL COMMENT '美元冻结金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB

-- 准备数据
insert into account_info values (1,'zs',10000,0);

CREATE DATABASE IF NOT EXISTS `bank02` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

USE `bank02`;

CREATE TABLE `account_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '户主姓名',
  `balance_cny` double DEFAULT NULL COMMENT '人民币帐户余额',
  `balance_usd` double DEFAULT NULL COMMENT '美元帐户余额',
  `frozen_cny` double DEFAULT NULL COMMENT '人民币冻结金额',
  `frozen_usd` double DEFAULT NULL COMMENT '美元冻结金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB


insert into account_info values (1,'ls',10000,0);
