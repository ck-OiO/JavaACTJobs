package com.github.ck_oio.javaatcjobs.fifthjob.homework10;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

// Hikari连接池改进
@Repository
public class HikariJdbcStudentRepository implements JdbcStudentRepository {
    @Autowired
    private DataSource dataSource;


    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
