package com.plf.diary.bio.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author panlf
 * @date 2022/2/17
 */
public class Client {
    public static void main(String[] args) {
        String filePath="D:\\TempData\\file\\hello.txt";
        //文件后缀
        String suffix = ".txt";
        try(InputStream inputStream = new FileInputStream(filePath)){
            Socket socket = new Socket("127.0.0.1",8888);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(suffix);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0){
                dataOutputStream.write(buffer,0,len);
            }
            dataOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
