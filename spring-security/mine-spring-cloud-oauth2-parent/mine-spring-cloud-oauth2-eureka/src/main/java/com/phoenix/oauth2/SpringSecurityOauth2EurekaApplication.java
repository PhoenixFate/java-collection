package com.phoenix.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author phoenix
 * @Date 2022/10/8 11:19
 * @Version 1.0.0
 */
@EnableEurekaServer //标识该模块为Eureka服务器
@SpringBootApplication
public class SpringSecurityOauth2EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityOauth2EurekaApplication.class, args);
    }
}
