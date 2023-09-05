package com.bytesRoom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class Part297EurekaServer2Application {

    public static void main(String[] args) {
        SpringApplication.run(Part297EurekaServer2Application.class,args);
    }

}
