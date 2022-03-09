package com.bytesRoom.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


//第一种属性注入方式

@Configuration
//通过@EnableConfigurationProperties(JdbcProperties.class)来声明要使用JdbcProperties这个类的对象
@EnableConfigurationProperties(JdbcProperties2.class )
public class JdbcConfiguration2 {

    //然后你可以通过以下方式注入JdbcProperties：
    //1.@Autowired注入
   // @Autowired
   // JdbcProperties jdbcProperties;


    //2.构造函数注入
    // private JdbcProperties prop;
    // public JdbcConfiguration2(Jdbcproperties prop){
    //     this.prop = prop;
    // }


    //3.声明有@Bean的方法参数注入
    @Bean
    public DataSource MyDataSource(JdbcProperties2 jdbcProperties2){
        System.out.println("jdbc2:"+ jdbcProperties2);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(jdbcProperties2.getDriverClassName());
        druidDataSource.setUrl(jdbcProperties2.getUrl());
        druidDataSource.setUsername(jdbcProperties2.getUsername());
        druidDataSource.setPassword(jdbcProperties2.getPassword());
        return druidDataSource;
    }

}
