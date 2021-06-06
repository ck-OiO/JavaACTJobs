package com.github.ck_oio.javaatcjobs.fifthjob.homework02;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/*
在容器启动阶段动态注入自定义bean, 接收AOP增强
 */
@Component
public class ManagerFactoryBean implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final DefaultListableBeanFactory bf = (DefaultListableBeanFactory) beanFactory;
        final BeanDefinitionBuilder managerBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Manager.class);
        // 为beanDefinition注入
        managerBeanDefinitionBuilder.addPropertyReference("secretary", "secretary");
        // 向容器注册
        bf.registerBeanDefinition("manager", managerBeanDefinitionBuilder.getRawBeanDefinition());
        // 直接注册一个singleton 的bean
        bf.registerSingleton("tianqi", new Employee().builder().name("tianqi").id(51).build());
    }
}
