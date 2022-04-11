package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Administrator
 * @version 1.0
 **/
@EnableEurekaServer //标识此工程是一个EurekaServer
@SpringBootApplication
public class XueChengGovernCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(XueChengGovernCenterApplication.class,args);
    }
}
