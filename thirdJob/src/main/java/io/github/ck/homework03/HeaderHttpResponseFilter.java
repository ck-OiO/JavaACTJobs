package io.github.ck.homework03;

import io.github.ck.gateway.filter.HttpResponseFilter;
import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("javaSocket-version", "11");
    }
}
