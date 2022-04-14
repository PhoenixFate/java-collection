package com.xuecheng.manage_course.config;

import com.xuecheng.framework.interceptor.FeignClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FeignClientConfig {

    //feign client 拦截器，用于将header继续向下转发
    @Bean
    public FeignClientInterceptor getFeignClientInterceptor() {
        return new FeignClientInterceptor();
    }

}
