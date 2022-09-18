package com.phoenix.web.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Auther: phoenix
 */
//Spring提供了一个@EnableTransactionManagement 注解以在配置类上开启声明式事务的支持。添加该注解后，Spring容器会自动扫描被@Transactional注解的方法和类。简单开启事务管理：
@EnableTransactionManagement //开启注解事务管理，等价于xml配置方式的 <tx:annotation-driven />
@MapperScan("com.phoenix.web.mapper") //扫描所有的mapper接口，这个也可以放在MainApplication头上
@Configuration
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
