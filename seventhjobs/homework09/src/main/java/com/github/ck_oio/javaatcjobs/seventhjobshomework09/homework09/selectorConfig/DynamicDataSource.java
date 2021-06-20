package com.github.ck_oio.javaatcjobs.seventhjobshomework09.homework09.selectorConfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * spring 根据该类型指定数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 设置默认数据源和数据源选项.
     *
     * @param defaultTargetDataSource
     * @param targetDataSources
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * 获取数据源对象时, 通过该方法获取数据源对象的key.
     * @return
     */
    @Override
    public Object determineCurrentLookupKey() {
        return typeHolder.get();
    }

    // 当前线程中数据源对应的key
    private static ThreadLocal<DataSourceType> typeHolder = new ThreadLocal<>();

    public static void setDataSourceType(DataSourceType type){
        typeHolder.set(type);
    }

    public static void removeDataSourceType(){
         typeHolder.remove();
    }

}
