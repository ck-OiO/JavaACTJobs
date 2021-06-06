package com.github.ck_oio.javaatcjobs.fifthjob.homework02;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
/*
如果xml配置bean的信息比较麻烦, 推荐使用这个方法.
 */
@Data
public class EmployeeFactoryBean implements FactoryBean<Employee> {

    private String employeeInfo;

    @Override
    public Employee getObject() throws Exception {
        final String[] info = employeeInfo.split(":");
        return Employee.builder().name(info[0]).id(Long.parseLong(info[1])).build();
    }

    @Override
    public Class<?> getObjectType() {
        return Employee.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
