package com.phoenix.compare;

/**
 * @Author phoenix
 * @Date 2023/3/28 14:16
 * @Version 1.0.0
 */
/**
 * [Employee(name=John, age=25, salary=3000.0, mobile=9922001),
 * Employee(name=Ace, age=22, salary=2000.0, mobile=5924001),
 * Employee(name=Keith, age=35, salary=4000.0, mobile=3924401)]
 */
class Employee {
    String name;
    int age;
    double salary;
    long mobile;

    // constructors, getters & setters


    public Employee(String name, int age, double salary, long mobile) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", salary=").append(salary);
        sb.append(", mobile=").append(mobile);
        sb.append('}');
        return sb.toString();
    }
}

