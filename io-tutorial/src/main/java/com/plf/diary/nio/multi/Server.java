package com.plf.diary.nio.multi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author panlf
 * @date 2022/3/8
 */
public class Server {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 9999;

    public Server() {
        try {
            selector  = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            //设置非阻塞通信模式
            serverSocketChannel.configureBlocking(false);
            //把通道注册到选择器上去，并且开始指定接收链接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void listen(){
        try {
            while (selector.select() > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();

                    if(selectionKey.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }else if(selectionKey.isReadable()){
                        readClientData(selectionKey);
                    }
                    iterator.remove();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readClientData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(byteBuffer);
            if(count > 0){
                byteBuffer.flip();
                String msg = new String(byteBuffer.array(),0,byteBuffer.remaining());
                System.out.println("接收到了客户端消息: "+msg);
                sendMsgToAllClient(msg,socketChannel);
            }
        }catch (Exception e){
            try {
                assert socketChannel != null;
                System.out.println("有人离线了~"+socketChannel.getRemoteAddress());
                selectionKey.cancel(); //取消注册
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void sendMsgToAllClient(String msg, SocketChannel socketChannel) throws IOException {
        System.out.println("服务端开始转发这个消息: 当前处理的线程: "+ Thread.currentThread().getName());
        for(SelectionKey selectionKey : selector.keys()){
            Channel channel = selectionKey.channel();
            if(channel instanceof SocketChannel && channel != socketChannel){
                ByteBuffer byteBuffer  = ByteBuffer.wrap(msg.getBytes());
                ((SocketChannel)channel).write(byteBuffer);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }

}
