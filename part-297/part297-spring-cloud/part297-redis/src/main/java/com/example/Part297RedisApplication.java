package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.example.mapper")
@EnableDiscoveryClient
public class Part297RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(Part297RedisApplication.class, args);
    }

}
