package com.example.mybatisplusdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mybatisplusdemo.pigxx.mapper")
public class Part297MybatisPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Part297MybatisPlusDemoApplication.class, args);
    }

}
