package com.plf.diary.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author panlf
 * @date 2023/4/30
 */
public class ByteStream {
    public static void main(String[] args) throws Exception {
        copyFile2();
    }

    public static void startWrite() throws Exception {
        /*
         *  字节输出流
         *   1、创建字节输出流对象
         *       细节1：参数是字符串表示的路径或者是File对象都是可以的
         *       细节2：如果文件不存在会创建一个新的文件，但要保证父级路径是存在的
         *       细节3：如果文件已经存在，则会清空文件
         *   2、写数据
         *       细节：write方法的参数是整数，但是实际上写到本地文件中的是整数在ASCII上对应的字符
         *
         *   3、释放资源
         *       每次使用完流之后都要释放资源
         * */

        //1.创建对象
        FileOutputStream fos = new FileOutputStream("D:\\TempData\\file\\1.txt");

        // 是否追加
        // FileOutputStream fos = new FileOutputStream("D:\\TempData\\file\\1.txt",true);

        //2.写出数据
        fos.write(57);
        //换行写 Windows \r\n
        //      Linux \n
        //      Mac   \r
        fos.write("\r\n".getBytes());
        fos.write(55);

        //3.释放资源
        fos.close();
    }

    public static void startRead() throws Exception {

        /**
         * FileInputStream书写细节
         *  1、创建字节输入流对象
         *      细节1：如果文件不存在，就直接报错
         *
         *  2、读取数据
         *      细节1：一次读一个字节，读出来的是数据在ASCII上对应的数字
         *      细节2：读到文件末尾了，read方法返回-1
         *
         *  3、释放资源
         *      细节1：每次使用完流必须要释放资源
         *
         */

        FileInputStream fis = new FileInputStream("D:\\TempData\\file\\1.txt");
        //int b1 = fis.read();
        int b;

        while ((b=fis.read())!=-1){
            System.out.println(b);
        }


        //System.out.println(b1);

        fis.close();
    }

    public static void copyFile() throws Exception {
        FileInputStream fis = new FileInputStream("D:\\TempData\\file\\1.txt");
        FileOutputStream fos = new FileOutputStream("D:\\TempData\\file\\1_copy.txt");

        int b;
        while((b=fis.read())!=-1){
            fos.write(b);
        }

        fis.close();
        fos.close();

    }

    public static void copyFile2() throws Exception {
        FileInputStream fis = new FileInputStream("D:\\TempData\\file\\1.txt");
        FileOutputStream fos = new FileOutputStream("D:\\TempData\\file\\2_copy.txt");

        int len;
        byte[] bytes = new byte[1024];
        while((len=fis.read(bytes))!=-1){
            fos.write(bytes,0,len);
        }

        fos.close();
        fis.close();

    }

    /**
     * 1、在计算机中，任意数据都是以二进制的形式来存储的
     * 2、计算机中最小的存储单元是一个字节
     * 3、ASCII字符集中，一个英文占一个字节
     * 4、简体中文版Windows，默认使用GBK字符集
     * 5、GBK字符集完全兼容ASCII字符集
     *  一个英文占一个字节，二进制第一位是0
     *  一个中文占两个字节，二进制高位字节的第一位是1
     * 6、Unicode字符集的UTF-8编码格式
     *  一个英文占一个字节，二进制第一位是0，转成十进制是正数
     *  一个中文占三个字节，二进制第一位是1，第一个字节转成十进制是负数
     */

    /*
    * Java中编码的方式
    *   public byte[] getBytes()                    使用默认方式进行编码
    *   public byte[] getBytes(String charsetName)  使用指定方式进行编码
    *
    * Java中解码的方式
    *   String(byte[] bytes)                        使用默认方式进行解码
    *   String(byte[] bytes,String charsetName)     使用指定方式进行解码
    * */
}
