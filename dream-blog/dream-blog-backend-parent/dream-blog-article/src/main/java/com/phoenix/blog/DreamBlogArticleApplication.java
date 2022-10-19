package com.phoenix.blog;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author phoenix
 * @Date 2022/10/10 10:45
 * @Version 1.0.0
 */
@EnableFeignClients // 扫描@FeignClient接口进行远程调
@EnableDiscoveryClient // spring cloud原生注解 开启nacos服务注册与发现
@EnableSwagger2Doc // 开启swagger
@SpringBootApplication
public class DreamBlogArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamBlogArticleApplication.class, args);
    }

}
