package com.github.ck_oio.javaatcjobs.fifthjob.homework10;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 导入application.yml 中数据库的配置
@EnableConfigurationProperties(DataBaseInfo.class)
@Component
public class DataBaseUtils {
    @Autowired
    private DataBaseInfo info;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
    }


    /**
     * 删除student 表
     * @throws SQLException
     */
    @PreDestroy
    public void deleteData() throws SQLException {
        getConnection().createStatement().executeUpdate("drop table student");
    }

}
