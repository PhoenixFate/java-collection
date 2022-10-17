package com.phoenix.blog.question.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement // 开启事务管理
@MapperScan("com.phoenix.blog.question.mapper") // 扫描mapper接口
@Configuration
public class MyBatisPlusConfig {

    /**
     * 分页插件
     *
     * @return 分页插件实例
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
