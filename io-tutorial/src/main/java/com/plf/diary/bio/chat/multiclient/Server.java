package com.plf.diary.bio.chat.multiclient;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现服务端同时接收多个客户端的Socket通信
 * @author panlf
 * @date 2022/2/17
 */
public class Server {
    /*
        1、每个Socket接收到，都会创建一个线程，线程的竞争、切换上下文影响性能
        2、每个线程都会占用栈空间和CPU资源
        3、并不是每个socket都进行IO操作，无意义的线程处理
        4、客户端的并发访问增加时。服务端将呈现1:1的线程开销，访问量越大，系统将发生线程栈溢出，线程创建失败，
        最终导致进程宕机或者僵死，从而不能对外提供服务。


     */
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(9999);

            while (true){
                Socket socket = serverSocket.accept();
                new ServerThreadReader(socket).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
