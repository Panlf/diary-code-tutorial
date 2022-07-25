package com.plf.diary.advance.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author panlf
 * @date 2022/7/25
 */
public class MysqlProxyServerVerticle extends AbstractVerticle {

    private final int port = 3306;
    private final String host = "127.0.0.1";

    private static final Logger log = LoggerFactory.getLogger(MysqlProxyServerVerticle.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MysqlProxyServerVerticle.class.getName());
    }

    @Override
    public void start() throws Exception {
        // 创建代理服务器
        NetServer netServer = vertx.createNetServer();
        NetClient netClient = vertx.createNetClient();
        netServer.connectHandler(netSocket -> netClient.connect(port,host,netSocketAsyncResult -> {
            if(netSocketAsyncResult.succeeded()){
                new MysqlProxyConnection(netSocket, netSocketAsyncResult.result()).proxy();
            }else {
                log.error(netSocketAsyncResult.cause().getMessage(), netSocketAsyncResult.cause());
                netSocket.close();
            }
        })).listen(8000, listenResult -> {//代理服务器的监听端口
            if (listenResult.succeeded()) {
                //成功启动代理服务器
                log.info("Mysql proxy server start up.");
            } else {
                //启动代理服务器失败
                log.error("Mysql proxy exit. because: " + listenResult.cause().getMessage(), listenResult.cause());
                System.exit(1);
            }
        });
    }
}

