package com.plf.diary.bio.file;

import com.plf.diary.bio.chat.multiclient.ServerThreadReader;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author panlf
 * @date 2022/2/21
 */
public class Server {
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true){
                Socket socket = serverSocket.accept();
                new ServerReaderThread(socket).start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
