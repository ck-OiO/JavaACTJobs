package com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.selectorConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 选择数据源的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceSelector {
    DataSourceType value();
}
