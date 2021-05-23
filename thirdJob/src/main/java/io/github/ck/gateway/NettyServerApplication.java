package io.github.ck.gateway;


import io.github.ck.gateway.inbound.HttpInboundServer;
import io.github.ck.servers.BackendServeresStart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NettyServerApplication {
    
    public final static String GATEWAY_NAME = "NIOGateway";
    public final static String GATEWAY_VERSION = "3.0.0";

    public final static int proxyPort = 8080;
    public final static int[] serverPorts = {8087, 8088, 8089};

    public static void main(String[] args) {
        //后端服务器启动
        new BackendServeresStart(NettyServerApplication.serverPorts);

        List<String> serverAddrs = new ArrayList<>();
        String serverHost = "http://localhost";
        Arrays.stream(serverPorts).forEach(p->serverAddrs.add(serverHost + ":" + p));
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" starting...");
        HttpInboundServer server = new HttpInboundServer(proxyPort, serverAddrs);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + proxyPort + " for server:" + server.toString());
        try {
            // netty服务器启动
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
