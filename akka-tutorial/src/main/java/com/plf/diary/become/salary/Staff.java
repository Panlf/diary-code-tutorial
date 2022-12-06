package com.plf.diary.become.salary;

/**
 * @author panlf
 * @date 2022/12/6
 */
public class Staff {
    private final String name;
    private final double salary;
    public Staff(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}
