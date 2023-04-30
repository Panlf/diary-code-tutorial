package com.plf.diary.base;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;

/**
 * @author panlf
 * @date 2023/4/30
 */
public class ConvertStream {
    /**
     *
     * 字符转换输入流 InputStreamReader
     * 字符转换输出流 OutputStreamWriter
     *
     * 转换流的作用是什么
     *  指定字符集读写数据（JDK11之后淘汰）
     *  字节流想要使用字符流中的方法了
     *
     */

    public static void main(String[] args) {

    }


    public static void reader() throws Exception {
        FileReader fileReader =
                new FileReader("D:\\TempData\\file\\1.txt", Charset.forName("GBK"));

        int ch;

        while((ch = fileReader.read()) != -1){
            System.out.println((char)ch);
        }

        fileReader.close();
    }

    public static void writer() throws Exception {
        FileWriter fileWriter = new FileWriter("D:\\TempData\\file\\1.txt",Charset.forName("GBK"));
        fileWriter.write("你好你好");

        fileWriter.close();
    }

    public static void readerWrite() throws Exception {
        FileReader fileReader =
                new FileReader("D:\\TempData\\file\\1.txt", Charset.forName("GBK"));

        FileWriter fileWriter = new FileWriter("D:\\TempData\\file\\1.txt",Charset.forName("GBK"));


        int b;

        while ((b = fileReader.read()) != -1){
            fileWriter.write(b);
        }

        fileWriter.close();
        fileReader.close();
    }
}
