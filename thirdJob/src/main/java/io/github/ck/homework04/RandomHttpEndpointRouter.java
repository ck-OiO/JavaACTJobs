package io.github.ck.homework04;

import io.github.ck.gateway.router.HttpEndpointRouter;

import java.util.List;
import java.util.Random;

public class RandomHttpEndpointRouter implements HttpEndpointRouter {
    /**
     * 三个服务器比重为2:3:5. 根据比重返回随机服务器地址
     * @param urls
     * @return
     */
    @Override
    public String route(List<String> urls) {
        final double ran = Math.random();
        if(ran >= 0.0 && ran <0.2)
            return urls.get(0);
        else if(ran >=0.2 && ran < 0.5)
            return urls.get(1);
        else
            return urls.get(2);
    }
}
