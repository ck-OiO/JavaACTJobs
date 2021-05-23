package io.github.ck.servers;

/**
 * 根据指定的端口, 启动服务器
 */
public class BackendServeresStart {
    public BackendServeresStart(int[] ports) {
            for (int port :
                    ports) {
                new BackendServer(port);
            }
    }

}
