package com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.selectorConfig;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 配置切面, 拦截对数据源选择的注解
 */
@Component
@Aspect
@Slf4j
public class DataSourceSelectorAspect {

    /**
     * 配置切点
     */
    @Pointcut("@annotation(com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.selectorConfig.DataSourceSelector)")
    public void createDBSelectorPointcut(){}

    /**
     * 根据切点的增强
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("createDBSelectorPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        final Method method = ((MethodSignature) point.getSignature()).getMethod();

        final DataSourceSelector selector = method.getAnnotation(DataSourceSelector.class);
        if(selector != null){
            DynamicDataSource.setDataSourceType(selector.value());
            log.info("current dataSource is:" + selector.value().name());
        }else{
            log.info("current dataSource is:" + DataSourceType.WRITER.name());
        }

        try{
            return point.proceed();
        }finally {
            DynamicDataSource.removeDataSourceType();
            log.info("remove dataSource type");
        }
    }
}
