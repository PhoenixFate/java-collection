package com.phoenix.workflow;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// 开启swagger接口文档
@EnableSwagger2Doc
@SpringBootApplication
public class WorkFlowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkFlowServiceApplication.class, args);
    }

}
