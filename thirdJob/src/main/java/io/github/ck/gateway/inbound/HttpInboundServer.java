package io.github.ck.gateway.inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;

import java.util.List;

@Data
public class HttpInboundServer {

    private int port;
    
    private List<String> proxyServers;

    public HttpInboundServer(int port, List<String> proxyServers) {
        this.port=port;
        this.proxyServers = proxyServers;
    }

    public void run() throws Exception {

        // 一个EventLoop对应一个线程, 间接的为每个group设置线程数
        EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 128)
                    // 不采用nagle 优化算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 保持连接不关闭
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 重复使用本地地址
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    //设置接收缓冲区
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    // 设置发送缓冲区
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    // 重复使用端口
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    // TCP 主动探测控线连接有效性
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 为Channel分配自适应接收缓冲区分配器
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new HttpInboundInitializer(this.proxyServers));

            Channel ch = b.bind(port).sync().channel();
            System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
