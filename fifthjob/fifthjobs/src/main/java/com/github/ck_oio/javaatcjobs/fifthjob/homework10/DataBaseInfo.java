package com.github.ck_oio.javaatcjobs.fifthjob.homework10;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DataBaseInfo {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
