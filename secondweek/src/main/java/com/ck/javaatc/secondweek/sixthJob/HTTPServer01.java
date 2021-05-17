package com.ck.javaatc.secondweek.sixthJob;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer01 {
    private static final int port = 8801;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        while(true){
            Socket socket = ss.accept();
            String msg = "hello,nio1";
            responseMsg(socket, msg);
            socket.close();
        }

    }

    private static void responseMsg(Socket socket, String msg) throws IOException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-Type:text/html;charset=utf-8");
        pw.println("Content-Length:" + msg.getBytes().length);
        pw.println();
        pw.write(msg);
        pw.close();
    }
}
