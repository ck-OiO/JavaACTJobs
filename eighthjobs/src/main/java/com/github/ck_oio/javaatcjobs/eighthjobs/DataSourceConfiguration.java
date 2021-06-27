package com.github.ck_oio.javaatcjobs.eighthjobs;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * 创建ShardingSphere 数据库
 */
@Configuration
public class DataSourceConfiguration {

    @Autowired
    private ResourceLoader resourceLoader;
    @Profile("homework06")
    @Bean
    @Primary
    public DataSource dataSource() throws IOException, SQLException {
        return YamlShardingSphereDataSourceFactory.createDataSource(resourceLoader.getResource("classpath:homework06/config-sharding.yaml").getFile());
    }


}
