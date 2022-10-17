package com.phoenix.blog;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwagger2Doc // 开启swagger功能
@SpringBootApplication
public class DreamBlogQuestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamBlogQuestionApplication.class, args);
    }

}
