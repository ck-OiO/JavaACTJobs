package com.github.ck_oio.javaatcjobs.depschool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SchoolProperties.class)
public class SchoolAutoConfig {

    @Autowired
    private SchoolProperties schoolProperties;

    @Bean
    @ConditionalOnProperty(prefix = "school", value = "enabled", havingValue = "true")
    public School school(){
        final School school = new School();
        school.setKlasses(schoolProperties.getKlasses());
        return school;
    }

}
