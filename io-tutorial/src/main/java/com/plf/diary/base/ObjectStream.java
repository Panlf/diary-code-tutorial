package com.plf.diary.base;

import java.io.*;

/**
 * @author panlf
 * @date 2023/5/1
 */
public class ObjectStream {

    public static void main(String[] args) throws Exception {
        write();
        read();
    }

    //序列化
    public static void write() throws Exception{
        Student student = new Student("Nana",1);

        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(new FileOutputStream("D:\\TempData\\file\\object.txt"));


        objectOutputStream.writeObject(student);

        objectOutputStream.close();
    }

    //反序列化
    public static void read() throws Exception {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new FileInputStream("D:\\TempData\\file\\object.txt"));
        Object o = objectInputStream.readObject();

        System.out.println(o);

        objectInputStream.close();
    }
}
