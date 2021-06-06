package com.github.ck_oio.javaatcjobs.depschool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "school")
public class SchoolProperties {
    private List<Klass> klasses;
}
