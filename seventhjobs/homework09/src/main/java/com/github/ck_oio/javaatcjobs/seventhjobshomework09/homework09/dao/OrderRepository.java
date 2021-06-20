package com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.dao;

import com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.Order;
import com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.selectorConfig.DataSourceSelector;
import com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.selectorConfig.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String insertOrderPS = "insert into t_order(f_user_id, f_total_value, f_address) values(?, ?, ?)";

    private static final String queryOrderById = "select f_order_id, f_user_id, f_create_at, f_total_value, f_address, f_status from t_order where f_order_id = ?";

    /**
     * 使用默认的写数据源
     *
     * @param order
     * @return
     */
    public int insert(Order order) {
        return jdbcTemplate.update(insertOrderPS, order.getUserId(), order.getTotalValue(), order.getAddress());
    }

    /**
     * 指定写数据源
     *
     * @param orders
     * @return
     */
    @DataSourceSelector(DataSourceType.WRITER)
    public int[] insertOrders(List<Object[]> orders) {
        return jdbcTemplate.batchUpdate(insertOrderPS, orders);
    }

    /**
     * 指定读数据源
     *
     * @param id
     * @return
     */
    @DataSourceSelector(DataSourceType.READER)
    public Order findById(int id) {
        return jdbcTemplate.queryForObject(queryOrderById,
                new RowMapper<Order>() {
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
                }, id);
    }
}
