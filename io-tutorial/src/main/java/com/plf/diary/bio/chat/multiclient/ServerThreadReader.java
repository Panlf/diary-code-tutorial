package com.plf.diary.bio.chat.multiclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author panlf
 * @date 2022/2/17
 */
public class ServerThreadReader extends Thread {
    private Socket socket;

    public ServerThreadReader(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            while ((msg = bufferedReader.readLine())!=null){
                System.out.println("服务端接收到:"+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
