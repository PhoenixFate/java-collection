package com.xuecheng.manage_course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator
 * @version 1.0
 **/
// @SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootApplication()
@EntityScan("com.xuecheng.framework.domain.course")//扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages = {"com.xuecheng.manage_course"})//扫描本工程的所有类
@ComponentScan(basePackages = {"com.xuecheng.framework"})//扫描common下的所有类
@EnableDiscoveryClient //表示一个EurekaClient从EurekaServer中发现服务
@EnableFeignClients //开启feignClient
public class XueChengManageCourseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(XueChengManageCourseApplication.class, args);
    }
}
