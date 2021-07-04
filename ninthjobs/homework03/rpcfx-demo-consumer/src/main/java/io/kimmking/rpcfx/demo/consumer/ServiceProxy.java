package io.kimmking.rpcfx.demo.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Aspect
@Component
public class ServiceProxy {

    private static final String serviceProviderUrl = "localhost";
    private static final int port = 8080;

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    @Pointcut("execution(* io.kimmking.rpcfx.demo.api.*Service.find*ById(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        final String methodName = ((MethodSignature) point.getSignature()).getMethod().getName();
        final String targetName = point.getTarget().getClass().getInterfaces()[0].getName();
        final Object[] args = point.getArgs();

        final RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(targetName);
        request.setMethod(methodName);
        request.setParams(args);
        post(request);
        return null;
    }

    private void post(RpcfxRequest request) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(serviceProviderUrl, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new RPCfxClientChannelInboudHandler(JSON.toJSONString(request)));
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}


class RPCfxClientChannelInboudHandler extends
        SimpleChannelInboundHandler<ByteBuf> {

    private String postBody;
    public RPCfxClientChannelInboudHandler(String postBody){
        this.postBody = postBody;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws URISyntaxException {
        final URI uri = new URI("/");
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uri.toASCIIString(), Unpooled.wrappedBuffer(postBody.getBytes(CharsetUtil.UTF_8)));
        request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
        ctx.writeAndFlush(request);
//        ctx.writeAndFlush(Unpooled.copiedBuffer(postBody,
//                CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        final RpcfxResponse response = JSON.parseObject(in.toString(CharsetUtil.UTF_8), RpcfxResponse.class);
        System.out.println("Client received: ");
        // 没有异常
        if(response.isStatus()){
            System.out.println(response.getResult());
        } else {
            response.getException().printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}