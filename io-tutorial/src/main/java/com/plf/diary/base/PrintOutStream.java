package com.plf.diary.base;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author panlf
 * @date 2023/5/3
 */
public class PrintOutStream {
    /**
     * 有字节打印流和字符打印流两种
     * 打印流不操作数据源，只能操作目的地
     * 字节打印流：默认自动刷新，特有的println自动换行
     * 字符打印流：自动刷新需要开启，特有的println自动换行
     *
     */

    public static void main(String[] args) throws Exception {
        print2();
    }


    public static void print() throws Exception {
        PrintStream printStream =
                new PrintStream(new FileOutputStream("D:\\TempData\\file\\print.txt"),true, StandardCharsets.UTF_8);

        printStream.println(97);//写出 + 自动刷新 + 自动换行

        printStream.print(true);

        printStream.println();

        printStream.printf("%s爱上了%s","阿珍","阿强");

        printStream.close();
    }

    public static void print1() throws Exception {
        PrintWriter printWriter = new PrintWriter(new FileWriter("D:\\TempData\\file\\print.txt"),true);

        printWriter.print("你好你好");


        printWriter.close();
    }

    public static void print2() throws Exception {
        PrintStream printStream = System.out;


        printStream.print("你好你好");


        printStream.close();
    }
}
