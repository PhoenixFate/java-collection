package com.bytesRoom.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class JdbcConfiguration3 {

    /**
     * 事实上，如果一段属性只有一个Bean需要使用，我们无需将其注入到一个类（JdbcProperties）中。而是直接在需要的地方声明即可：
     * @return
     */
    //第二种属性注入方式
    @Bean
    @ConfigurationProperties(prefix = "jdbc3")
    public DataSource MyDataSource(){
        return new DruidDataSource();
    }

}
