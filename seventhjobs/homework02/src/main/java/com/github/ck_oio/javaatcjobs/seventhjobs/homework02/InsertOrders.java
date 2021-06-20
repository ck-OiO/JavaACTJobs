package com.github.ck_oio.javaatcjobs.seventhjobs.homework02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InsertOrders {


    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate rbsJdbcTemplate;


    @Autowired
    @Qualifier("dataSource")
    private DataSource rbsDS;

    private static final String insertOrderPS = "insert into t_order(f_user_id, f_total_value, f_address) values(?, ?, ?)";

    /**
     * 每个sql 语句用一个事务
     * @param orders
     */
    public void insertByIterator(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            rbsJdbcTemplate.update(insertOrderPS,
                    orders.get(i).getUserId(),
                    orders.get(i).getTotalValue(),
                    orders.get(i).getAddress());
        }
    }

    /**
     * 所有insert 语句共用一个事务
     * @param orders
     */
    public void insertByIteratorInOneTx(List<Order> orders){
        try (var con = rbsDS.getConnection()) {
            final boolean autoCommitFlag = con.getAutoCommit();
            con.setAutoCommit(false);
            try (var ps = con.prepareStatement(insertOrderPS)) {
                for (int i = 0; i < orders.size(); i++) {
                    ps.setInt(1, orders.get(i).getUserId());
                    ps.setBigDecimal(2, orders.get(i).getTotalValue());
                    ps.setString(3, orders.get(i).getAddress());
                    ps.executeUpdate();
                    ps.clearParameters();
                }
            }
            con.commit();
            con.setAutoCommit(autoCommitFlag);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量插入
     *
     * jdbc 的url 连接加上rewriteBatchedStatements=true mysql 支持批量插入.
     * @param orders Object[0] 对应Order.userId, Object[1] 对应Order.totalValue, Object[2] 对应Order.address
     */
    public void insertByBatch(List<Object[]> orders) {
        rbsJdbcTemplate.batchUpdate(insertOrderPS, orders);
    }



    /**
     * 使用multiple values的insert语句插入
     * 需要在mysql配置文件中[mysql]和[mysqld] 中设置max_allowed_pocket 值大于一次请求的数据量大小.
     *
     * @param sql
     */
    public void insertByOneStatement(String sql) {
        rbsJdbcTemplate.update(sql);
    }





}
