package com.phoenix.blog;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author phoenix
 * @Date 2022/10/10 10:45
 * @Version 1.0.0
 */
@EnableSwagger2Doc //开启swagger
@SpringBootApplication
public class DreamBlogArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamBlogArticleApplication.class, args);
    }

}
