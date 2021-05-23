package io.github.ck.servers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 创建了一个固定大小的线程池处理请求
public class BackendServer {
    private static Logger logger = LoggerFactory.getLogger(BackendServer.class);
    public BackendServer(int port){
        new Thread(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(
                    // 作为Web server, IO比较多
                    Runtime.getRuntime().availableProcessors() * 2);
            try {
                final ServerSocket serverSocket = new ServerSocket(port);
                logger.info("服务{}启动", port);
                while (true) {
                    final Socket socket = serverSocket.accept();
                    executorService.execute(() -> service(socket, port));
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    private static void service(Socket socket, int port) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio" + port;
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException e) { // | InterruptedException e) {
            e.printStackTrace();
        }
    }
}