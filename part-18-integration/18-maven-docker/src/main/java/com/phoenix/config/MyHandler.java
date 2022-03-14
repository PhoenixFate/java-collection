package com.phoenix.config;

import org.springframework.stereotype.Component;

@Component
public class MyHandler {

    private JdbcProperties2 jdbcProperties2;

    MyHandler(JdbcProperties2 jdbcProperties2){
        this.jdbcProperties2 = jdbcProperties2;
    }

    public void test(){
        System.out.println("spring 可以通过构造函数注入对象："+ jdbcProperties2.getUrl());
    }

}
