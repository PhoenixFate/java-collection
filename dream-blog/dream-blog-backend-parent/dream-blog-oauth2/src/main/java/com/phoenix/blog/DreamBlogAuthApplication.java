package com.phoenix.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author phoenix
 * @Date 2022/10/21 17:52
 * @Version 1.0.0
 */
@EnableFeignClients // 扫描Feign接口
@EnableDiscoveryClient // 标识为nacos客户端
@SpringBootApplication
public class DreamBlogAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamBlogAuthApplication.class, args);
    }
}
