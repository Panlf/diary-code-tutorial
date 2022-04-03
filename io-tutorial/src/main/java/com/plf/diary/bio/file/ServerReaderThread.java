package com.plf.diary.bio.file;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * @author panlf
 * @date 2022/2/21
 */
public class ServerReaderThread extends Thread {
    private Socket socket;

    public ServerReaderThread(Socket socket){
        this.socket = socket;
    }
    private final String serverDir = "D:\\TempData\\copyTemp";

    @Override
    public void run() {
        try{
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String suffix = dataInputStream.readUTF();
            OutputStream outputStream = new FileOutputStream(serverDir+"\\"+ UUID.randomUUID().toString()+suffix);

            byte[] buffer = new byte[1024];
            int len;
            while((len = dataInputStream.read(buffer)) > 0){
                outputStream.write(buffer,0,len);
            }
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
