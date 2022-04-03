package com.plf.diary.bio.repost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author panlf
 * @date 2022/2/21
 */
public class ServerHandler extends Thread{

    private Socket socket;
    public ServerHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg ;
            while((msg = bufferedReader.readLine())!=null){
                sendMsgToAllClient(msg);
            }
        }catch (Exception e){
            System.out.println("当前有人下线了！");
            Server.allSocketOnLine.remove(socket);
        }
    }

    private void sendMsgToAllClient(String msg) throws IOException {
        for (Socket socket : Server.allSocketOnLine){
            PrintStream printStream  = new PrintStream(socket.getOutputStream());
            printStream.println(msg);
            printStream.flush();
        }
    }
}
