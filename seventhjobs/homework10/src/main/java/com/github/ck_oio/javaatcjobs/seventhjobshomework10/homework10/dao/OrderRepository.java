package com.github.ck_oio.javaatcjobs.seventhjobshomework10.homework10.dao;

import com.github.ck_oio.javaatcjobs.seventhjobshomework10.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String insertOrderWithoutId = "insert into t_order(f_user_id, f_total_value, f_address) values(?, ?, ?)";
    private static final String insertOrderWithId = "insert into t_order(f_order_id, f_user_id, f_total_value, f_address) values(?, ?, ?, ?)";

    private static final String queryAll = "select f_order_id, f_user_id, f_create_at, f_total_value, f_address, f_status from t_order";

    /**
     * ShardingSphere 在主键没有值时, 会自动使用雪花算法创建主键
     *
     * @param order
     * @return
     */
    public int insertWithoutId(Order order) {
        return jdbcTemplate.update(insertOrderWithoutId, order.getUserId(), order.getTotalValue(), order.getAddress());
    }

    public int insertWithId(Order order) {
        return jdbcTemplate.update(insertOrderWithId, order.getOrderId(), order.getUserId(), order.getTotalValue(), order.getAddress());
    }

    /**
     * 从ShardingShpere 中查询所有数据, 底层数据会在所有对应的数据库和表中查询.
     * @return
     */
    public List<Order> findAll() {
        return jdbcTemplate.query(queryAll, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int i) throws SQLException {
                final Order order = Order.builder().build();
                order.setOrderId(rs.getInt("f_user_id"));
                order.setOrderId(rs.getInt("f_order_id"));
                order.setCreateAt(rs.getDate("f_create_at"));
                order.setTotalValue(rs.getBigDecimal("f_total_value"));
                order.setAddress(rs.getString("f_address"));
                order.setStatus(rs.getByte("f_status"));
                return order;
            }
        });
    }
}

