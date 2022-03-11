package com.plf.diary.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @author panlf
 * @date 2022/2/22
 */
public class BufferTest {
    public static void main(String[] args) {
        // 1、分配一个缓冲区，容量设置为10
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        System.out.println("================");
        // 2、put往缓冲区添加数据
        String name = "JavaNio";
        byteBuffer.put(name.getBytes());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        System.out.println("================");
        //3、Buffer flip() 将缓冲区的界限设置为当前位置，并将当前位置设值为0 可读模式
        byteBuffer.flip();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("================");

        //4、get数据的读取
        char ch = (char) byteBuffer.get();
        System.out.println(ch);
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("================");

        byteBuffer.clear();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println((char)byteBuffer.get());

    }
}
