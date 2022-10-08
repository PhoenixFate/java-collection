package com.phoenix.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author phoenix
 * @Date 2022/9/29 16:23
 * @Version 1.0.0
 */
@EnableEurekaClient //标识为eureka客户端
@SpringBootApplication
public class SpringSecurityOauth2AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityOauth2AuthApplication.class, args);
    }

}
