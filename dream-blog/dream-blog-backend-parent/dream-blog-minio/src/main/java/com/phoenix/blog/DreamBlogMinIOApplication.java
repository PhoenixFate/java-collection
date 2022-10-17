package com.phoenix.blog;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author phoenix
 * @Date 2022/10/17 17:58
 * @Version 1.0.0
 */
@EnableSwagger2Doc //开启swagger
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DreamBlogMinIOApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamBlogMinIOApplication.class, args);
    }

}
