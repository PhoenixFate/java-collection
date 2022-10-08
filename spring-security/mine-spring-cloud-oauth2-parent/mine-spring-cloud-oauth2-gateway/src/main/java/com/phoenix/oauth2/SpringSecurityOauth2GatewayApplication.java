package com.phoenix.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author phoenix
 * @Date 2022/10/8 15:03
 * @Version 1.0.0
 */
@EnableZuulProxy //开启zuul的功能
@EnableEurekaClient //标识为eureka客户端
@SpringBootApplication
public class SpringSecurityOauth2GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityOauth2GatewayApplication.class, args);
    }

}
