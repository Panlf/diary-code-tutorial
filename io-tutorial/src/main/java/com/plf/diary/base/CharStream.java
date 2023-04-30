package com.plf.diary.base;

import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author panlf
 * @date 2023/4/30
 */
public class CharStream {
    public static void main(String[] args) throws Exception {
        //reader2();
        write();
    }

    public static void reader() throws Exception {
        //1.创建对象并关联本地文件
        FileReader fileReader = new FileReader("D:\\TempData\\file\\1.txt");

        //2.读取数据 read()
        //字符流的底层也是字节流，默认也是一个字节一个字节的读取的
        //如果遇到中文就会一次读取多个，GBK一次读两个字节，UTF-8一次读取三个字节

        //read()细节
        //1.read() 默认也是一个字节一个字节的读取的，如果遇到中文就会一次读取多个
        //2.在读取之后，方法的底层还会进行解码并转成十进制
        //  最终把这个十进制作为返回值
        //  这个十进制的数据也表示在字符集上的数字
        //  英文：文件里面二进制数据0110 0001
        //          read方法进行读取，解码并转成十进制97
        //  中文：文件里面的二进制数据 11100110 10110001 10001001
        //          read方法进行读取，解码并转成27721
        // 想看到中文汉字，就是把这些十进制数据，再进行强转就可以了

        int ch;

        while((ch = fileReader.read())!=-1){
            System.out.print((char)ch);
        }

        //3.释放资源
        fileReader.close();

    }

    public static void reader2() throws Exception {
        /**
         * 创建字符输入流对象
         *  底层：关联文件，并创建缓冲区（长度为8192的字节数组）
         *
         * 读取数据
         *  底层：
         *      1、判断缓冲区中是否有数据可以读取
         *      2、缓冲区没有数据：就从文件中获取数据，装到缓冲区中，每次尽可能装满缓冲区
         *          如果文件中也没有数据了，返回-1
         *      3、缓冲区有数据：就从缓冲区中读取。
         *          空参的read方法：一次读取一个字节，遇到中文一次读取多个字节，把字节解码并转成十进制返回
         *          有参的read方法：把读取字节，解码，强转三步合并了，强转之后的字符放到数组中。
         */
        FileReader fileReader = new FileReader("D:\\TempData\\file\\1.txt");

        char[] c = new char[10];

        int len;

        while((len=fileReader.read(c))!=-1){
            System.out.print(new String(c,0,len));
        }

        fileReader.close();
    }


    public static void write() throws Exception{

        FileWriter fileWriter = new FileWriter("D:\\TempData\\file\\1.txt",true);

        //将缓冲区中的数据，刷新到本地文件中
        fileWriter.flush();

        //释放资源/关流
        fileWriter.close();
    }

    public static void readWrite() throws Exception {


        FileReader fileReader = new FileReader("D:\\TempData\\file\\1.txt");
        //会把文件中的数据放到缓冲区中，缓冲区大小为8192个字节
        fileReader.read();

        //清空文件
        FileWriter fileWriter = new FileWriter("D:\\TempData\\file\\1.txt");

        //会把缓冲区中的数据全部读取完毕
        //但是只能读取缓冲区中的数据，文件中剩余的数据无法再次读取
        int ch;
        while ((ch=fileReader.read())!=-1){
            System.out.println((char)ch);
        }

        fileWriter.close();
        fileReader.close();

    }
}
