package com.phoenix.workflow;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 开启swagger接口文档
@EnableSwagger2Doc
@SpringBootApplication
public class Activity7WorkFlowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Activity7WorkFlowServiceApplication.class, args);
    }

}
