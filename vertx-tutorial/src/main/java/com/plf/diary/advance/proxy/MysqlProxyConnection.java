package com.plf.diary.advance.proxy;

import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author panlf
 * @date 2022/7/25
 */
public class MysqlProxyConnection {

    private static Logger log = LoggerFactory.getLogger(MysqlProxyConnection.class);

    private final NetSocket clientSocket;
    private final NetSocket serverSocket;

    public MysqlProxyConnection(NetSocket clientSocket, NetSocket serverSocket) {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;
    }

    public void proxy() {
        //当代理与mysql服务器连接关闭时，关闭client与代理的连接
        serverSocket.closeHandler(v -> clientSocket.close());
        //反之亦然
        clientSocket.closeHandler(v -> serverSocket.close());
        //不管那端的连接出现异常时，关闭两端的连接
        serverSocket.exceptionHandler(e -> {
            log.error(e.getMessage(), e);
            close();
        });
        clientSocket.exceptionHandler(e -> {
            log.error(e.getMessage(), e);
            close();
        });
        //当收到来自客户端的数据包时，转发给mysql目标服务器
        clientSocket.handler(serverSocket::write);
        //当收到来自mysql目标服务器的数据包时，转发给客户端
        serverSocket.handler(clientSocket::write);
    }

    private void close() {
        clientSocket.close();
        serverSocket.close();
    }
}

