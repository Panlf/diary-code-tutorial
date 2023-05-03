package com.plf.diary.base;

import java.io.Serializable;

/**
 * @author panlf
 * @date 2023/5/1
 */
public class Student implements Serializable {


    private static final long serialVersionUID = 5862412135047930589L;

    private String name;
    private Integer age;

    //transient 瞬态关键字
    //作用：不会把当前属性序列化到本地文件
    private transient String address;

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
