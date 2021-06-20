package com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.selectorConfig;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class CreateDataSoruces {
    /**
     * 写数据源相关配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "datasource.writer")
    public DataSourceProperties writeBatchedStatementsProp() {
        return new DataSourceProperties();
    }

    /**
     *  写数据源
     *
     * @return
     */
    @Bean
    public DataSource writeDataSource() {
        return writeBatchedStatementsProp().initializeDataSourceBuilder().build();
    }


    /**
     * 读数据源相关配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "datasource.reader")
    public DataSourceProperties readBatchedStatementsProp() {
        return new DataSourceProperties();
    }

    /**
     * 只读数据源
     *
     * @return
     */
    @Bean
    public DataSource readDataSource() {
        return readBatchedStatementsProp().initializeDataSourceBuilder().build();
    }

    /**
     * 创建动态数据源代理
     * @return
     */
    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(){
        final Map dataSourceMap = Map.of(
                DataSourceType.WRITER, writeDataSource(),
                DataSourceType.READER, readDataSource());
        return new DynamicDataSource(writeDataSource(), dataSourceMap);
    }

}
