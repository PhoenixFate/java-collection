package com.phoenix.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author phoenix
 * @Date 10/5/22 23:43
 * @Version 1.0
 */
@EnableEurekaClient // 标识为Eureka客户端
@SpringBootApplication
public class SpringSecurityOauth2ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityOauth2ProductApplication.class, args);
    }
}
