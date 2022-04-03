package com.plf.diary.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author panlf
 * @date 2022/3/6
 */
public class ChannelTest {
    public static void main(String[] args) {
        ChannelTest channelTest = new ChannelTest();
        channelTest.copy();
    }


    private void transferFromTest() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("data01.txt");
        FileChannel isChannel  = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("data02.txt");
        FileChannel osChannel = fileOutputStream.getChannel();

        //osChannel.transferFrom(isChannel,isChannel.position(),isChannel.size());
        isChannel.transferTo(isChannel.position(),isChannel.size(),osChannel);


        isChannel.close();
        osChannel.close();
    }

    private void transferToTest() throws Exception{

    }

    private void multiChannel(){
        try{
            FileInputStream fileInputStream = new FileInputStream("D:\\TempData\\temp\\hello.txt");
            FileChannel fileChannel = fileInputStream.getChannel();
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\TempData\\temp\\hello_copy.txt");
            FileChannel fileOutChannel = fileOutputStream.getChannel();

            ByteBuffer buffer1 = ByteBuffer.allocate(4);
            ByteBuffer buffer2 = ByteBuffer.allocate(1024);
            ByteBuffer[] buffers = {buffer1,buffer2};

            fileChannel.read(buffers);
            for(ByteBuffer buffer:buffers){
                buffer.flip();
                System.out.println(new String(buffer.array(),0,buffer.remaining()));
            }
            fileOutChannel.write(buffers);
            fileChannel.close();
            fileOutChannel.close();
        }catch (Exception e){

        }
    }

    private void copy(){
        try {
            File srcFile = new File("D:\\TempData\\img\\bizhi.jpg");
            File destFile = new File("D:\\TempData\\img\\bizhi_copy.jpg");
            FileInputStream fileInputStream = new FileInputStream(srcFile);

            FileOutputStream fileOutputStream = new FileOutputStream(destFile);

            FileChannel  fileInputChannel = fileInputStream.getChannel();
            FileChannel fileOutputChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (true){
                //先清空缓冲然后再写入数据到缓冲区
                byteBuffer.clear();

                int flag = fileInputChannel.read(byteBuffer);
                if(flag == -1){
                    break;
                }
                //已经读取了数据，把缓冲区的模式切换成可读模式
                byteBuffer.flip();
                fileOutputChannel.write(byteBuffer);

            }

            fileInputChannel.close();
            fileOutputChannel.close();
        }catch (Exception e){

        }


    }


    private void write(){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\TempData\\file\\hello.txt");

            FileChannel fileChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            byteBuffer.put("hello channel".getBytes());
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            fileChannel.close();

        }catch (Exception e){

        }
    }

    private void read(){
        try{

            FileInputStream fileInputStream = new FileInputStream("D:\\TempData\\file\\hello.txt");
            FileChannel fileChannel = fileInputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            fileChannel.read(byteBuffer);
            byteBuffer.flip();

            String result =new String(byteBuffer.array(),0,byteBuffer.remaining());

            System.out.println(result);

        }catch (Exception e){

        }
    }

}
