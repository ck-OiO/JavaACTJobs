package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.api.RpcfxResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DemoResolver implements RpcfxResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据bean id 返回指定的bean.
     * @param serviceClass
     * @return
     */
    @Override
    public Object resolve(String serviceClass) {
        return this.applicationContext.getBean(serviceClass);
//        try {
//            final Class<?> clazz = Class.forName(serviceClass);
//            applicationContext.getBean(clazz);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
