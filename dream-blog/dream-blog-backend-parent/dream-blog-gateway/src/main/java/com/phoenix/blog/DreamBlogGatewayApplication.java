package com.phoenix.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author phoenix
 * @Date 2022/10/26 13:52
 * @Version 1.0.0
 */
@EnableDiscoveryClient //nacos客户端
@SpringBootApplication
public class DreamBlogGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamBlogGatewayApplication.class, args);
    }

}
