package com.bytesRoom.config;

import org.springframework.stereotype.Component;

@Component
public class Myhandler {

    private JdbcProperties2 jdbcProperties2;

    Myhandler(JdbcProperties2 jdbcProperties2){
        this.jdbcProperties2 = jdbcProperties2;
    }

    public void test(){
        System.out.println("spring 可以通过构造函数注入对象："+ jdbcProperties2.getUrl());
    }

}
