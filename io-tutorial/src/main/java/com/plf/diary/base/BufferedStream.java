package com.plf.diary.base;

import java.io.*;

/**
 * @author panlf
 * @date 2023/4/30
 */
public class BufferedStream {
    public static void main(String[] args) {

    }

    public static void bufferStream() throws Exception {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream("D:\\TempData\\file\\1.txt")
        );

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream("D:\\TempData\\file\\1_buffer.txt")
        );

        int b;

        while((b=bufferedInputStream.read()) != -1){
            bufferedOutputStream.write(b);
        }

        bufferedOutputStream.close();
        bufferedInputStream.close();
    }


    public static void bufferStream2() throws Exception {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream("D:\\TempData\\file\\1.txt")
        );

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream("D:\\TempData\\file\\1_buffer2.txt")
        );

        int len;

        byte[] bytes = new byte[1024];

        while((len=bufferedInputStream.read(bytes)) != -1){
            bufferedOutputStream.write(bytes,0,len);
        }

        bufferedOutputStream.close();
        bufferedInputStream.close();
    }


    public static void bufferRead() throws Exception {
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader("D:\\TempData\\file\\1.txt"));

        String line;

        while((line = bufferedReader.readLine()) != null){
            System.out.println(line);
        }

        bufferedReader.close();
    }

    public static void bufferWriter() throws Exception {
        BufferedWriter bufferedWriter =
                new BufferedWriter(new FileWriter("D:\\TempData\\file\\1.txt"));

        bufferedWriter.write("11");

        bufferedWriter.close();
    }
}
