package com.plf.test;

import java.util.HashMap;

/**
 * @author panlf
 * @date 2022/2/22
 */
public class StaticTest {
    public static final HashMap<String,String> lines = new HashMap<>(2);
    static {
        System.out.println(1);
    }

    public static void get(){
        System.out.println(2);
    }

    public void getOne(){
        System.out.println(3);
    }

    public static void main(String[] args) {
        StaticTest staticTest1 = new StaticTest();
        StaticTest staticTest2 = new StaticTest();
        System.out.println(staticTest1.toString());
        System.out.println(staticTest2.toString());
        System.out.println(StaticTest.lines);
        System.out.println(StaticTest.lines);
    }
}
