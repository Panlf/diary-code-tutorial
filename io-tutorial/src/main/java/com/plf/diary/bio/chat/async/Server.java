package com.plf.diary.bio.chat.async;

import com.plf.diary.bio.chat.multiclient.ServerThreadReader;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author panlf
 * @date 2022/2/17
 */
public class Server {
    /*
        1、伪异步IO采用线程池实现，因此避免了每个请求创建一个独立线程造成线程资源耗尽的问题，但由于底层依然是采用的
        同步阻塞模型，因此无法从根本上解决问题
        2、如果单个消息处理的缓慢，或者服务器线程池中的全部线程都被堵塞，那么后续socket的i/o消息都将在队列中排队。
        新的Socket请求将被拒绝，客户端会发生大量连接超时

     */
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(9999);
            HandlerSocketServerPool socketServerPool = new HandlerSocketServerPool(6,20);
            while (true){
                Socket socket = serverSocket.accept();

                Runnable target = new ServerRunnableTarget(socket);

                socketServerPool.execute(target);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
