package com.bytesRoom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.bytesRoom.mapper")
public class Part13UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(Part13UserApplication.class, args);
    }

}
