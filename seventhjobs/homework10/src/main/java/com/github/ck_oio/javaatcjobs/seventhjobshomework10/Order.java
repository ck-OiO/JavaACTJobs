package com.github.ck_oio.javaatcjobs.seventhjobshomework10;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Order {
/*
`f_order_id` int(11) NOT NULL,
  `f_user_id` int(11) DEFAULT NULL,
  `f_create_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `f_total_value` decimal(10,0) DEFAULT NULL COMMENT '该订单总额度',
  `f_address` varchar(255) DEFAULT NULL COMMENT '订单地址',
  `f_status` tinyint(4) DEFAULT '1' COMMENT '订单状态: 0:不可用  1:未支付  2:已支付 3:已发货 4:已到达 5:已签收',
 */
    private int orderId;
    private int userId;
    private Date createAt;
    private BigDecimal totalValue;
    private String address;
    private byte status;

}
