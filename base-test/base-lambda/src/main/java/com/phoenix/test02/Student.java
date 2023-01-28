package com.phoenix.test02;

/**
 * @Author phoenix
 * @Date 1/27/23 18:05
 * @Version 1.0
 */
public class Student {

    private  String no;  //学号
    private  String name;	//姓名
    //构造方法忽略
    //set、get 方法忽略


    public Student(String no, String name) {
        this.no = no;
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
