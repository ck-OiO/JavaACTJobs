package com.github.ck_oio.javaatcjobs.fifthjob.homework02;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/*
@SpringBootApplication 中包含有@ComponentScan 可以扫描注解有@Component 的类生成bean.
该bean默认懒加载为false, scope 为singleton, id为employee, 根据类型自动注入, 这里基本类型和字符串使用默认值.

@ComponentScan 也会自动扫描标注有@Controller, @Service, @Repository 类成为bean.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Employee {
    private String name;
    private long id;
}
