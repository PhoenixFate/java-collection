package com.phoenix.test02;

/**
 * @Author phoenix
 * @Date 1/27/23 18:06
 * @Version 1.0
 */
public class Teacher {

    private String no;  //教师号
    private String name; //姓名

    public Teacher() {
    }

    public Teacher(String no, String name) {
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
