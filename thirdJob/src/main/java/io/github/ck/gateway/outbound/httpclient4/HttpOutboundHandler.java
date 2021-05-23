package io.github.ck.gateway.outbound.httpclient4;


import io.github.ck.homework01.HttpClientUtils;
import io.github.ck.homework03.HeaderHttpResponseFilter;
import io.github.ck.gateway.filter.HttpRequestFilter;
import io.github.ck.gateway.filter.HttpResponseFilter;
import io.github.ck.gateway.router.HttpEndpointRouter;
import io.github.ck.homework04.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {

    private CloseableHttpAsyncClient httpclient;
    private ExecutorService proxyService;
    private List<String> backendUrls;

    HttpResponseFilter responseFilter = new HeaderHttpResponseFilter();
    HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public HttpOutboundHandler(List<String> backends) {
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());
    }

    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter requestFilter) {
        String backendUrl = router.route(this.backendUrls);
        requestFilter.filter(fullRequest, ctx);
        FullHttpResponse response = null;
        try {

            // 使用OkHttp 时, handle的调用者一直抛出"空指针异常" 不知道为什么
//            byte[] body = OkhttpOutboundHandler.getAsString(backendUrl).getBytes();
            final HttpHeaders headers = fullRequest.headers();
            final byte[] body = HttpClientUtils.getAsString(backendUrl, headers).getBytes(StandardCharsets.UTF_8);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));

            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", body.length);

            responseFilter.filter(response);

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
