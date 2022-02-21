package com.plf.diary.bio.repost;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * BIO模式下的端口转发
 * @author panlf
 * @date 2022/2/21
 */
public class Server {

    public static List<Socket> allSocketOnLine = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            while(true){
                Socket socket = serverSocket.accept();
                //把登录的客户端socket存入到一个在线集合中去
                allSocketOnLine.add(socket);
                //为当前登录成功的socket分配一个独立的线程来处理与之通信



            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
