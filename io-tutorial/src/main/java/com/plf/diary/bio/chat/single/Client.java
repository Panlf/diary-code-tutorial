package com.plf.diary.bio.chat.single;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author panlf
 * @date 2022/2/15
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",9999);
        OutputStream outputStream = socket.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        //区别printStream.print
        printStream.println("Hello World! 服务端，您好！");
        printStream.flush();
    }
}
