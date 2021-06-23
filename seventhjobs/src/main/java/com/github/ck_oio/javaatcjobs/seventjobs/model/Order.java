package com.github.ck_oio.javaatcjobs.seventjobs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    /*
     * f_order_id
    */
    private Long orderId;

    /*
     * f_user_id
    */
    private Long userId;

    /*
     * f_create_at
    */
    private Date createAt;

    /*
    * 该订单总额度
     * f_total_value
    */
    private BigDecimal totalValue;

    /*
    * 订单地址
     * f_address
    */
    private String address;

    /*
    * 订单状态: 0:不可用  1:未支付  2:已支付 3:已发货 4:已到达 5:已签收
     * f_status
    */
    private Byte status;


}