package com.phoenix.oauth2.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author phoenix
 * @Date 10/7/22 12:17
 * @Version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringSecurityOauth2OSSClientOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityOauth2OSSClientOneApplication.class, args);
    }

}
