package com.phoenix.blog.article.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author phoenix
 * @Date 2022/10/11 15:20
 * @Version 1.0.0
 */
@EnableTransactionManagement //开启事务管理
@MapperScan("com.phoenix.blog.article.mapper") //扫描mapper接口
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     *
     * @return 分页拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
