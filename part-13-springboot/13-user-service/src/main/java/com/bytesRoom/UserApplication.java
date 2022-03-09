package com.bytesRoom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.bytesRoom.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.bytesRoom.UserApplication.class, args);
    }

}
