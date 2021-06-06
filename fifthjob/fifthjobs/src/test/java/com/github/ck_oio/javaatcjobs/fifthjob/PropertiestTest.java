package com.github.ck_oio.javaatcjobs.fifthjob;

import com.github.ck_oio.javaatcjobs.depschool.Klass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@ConfigurationProperties(prefix = "school")
@SpringBootTest
public class PropertiestTest {
    private List<Klass> klasses;
    private String enabled;
    @Test
    public void testtest(){
        System.out.println(klasses);
    }
}
