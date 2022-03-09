package com.bytesRoom.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;


@Configuration
//传统的spring3的注解注入方式、并不是springboot特有的注入方式
@PropertySource("classpath:jdbc1.properties")
public class JdbcConfiguration1 {

    @Value("${jdbc1.url}")
    String url;
    @Value("${jdbc1.driverClassName}")
    String driverClassName;
    @Value("${jdbc1.username}")
    String username;
    @Value("${jdbc1.password}")
    String password;

    @Bean
    public DataSource MyDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }

}
