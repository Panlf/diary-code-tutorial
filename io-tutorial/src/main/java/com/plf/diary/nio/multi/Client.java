package com.plf.diary.nio.multi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author panlf
 * @date 2022/3/9
 */
public class Client {

    private Selector selector;
    private static int PORT = 9999;
    private SocketChannel socketChannel;


    public Client(){
        try{
            //创建选择器
            selector = Selector.open();
            //链接服务端
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",PORT));
            //设置非阻塞模式
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端准备完成");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        new Thread(()->{
            try {
                client.readInfo();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String message = scanner.nextLine();
            client.sendToServer(message);
        }

    }

    private void sendToServer(String message) {
        try {
            socketChannel.write(ByteBuffer.wrap(("客户端说："+message).getBytes()));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readInfo() throws IOException {
        while (selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);
                    System.out.println(new String(byteBuffer.array()).trim());
                }
                iterator.remove();
            }
        }
    }


}
