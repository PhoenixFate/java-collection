package com.xuecheng.manage_media_process.config;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfig {

    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions
                .builder()
                .socketTimeout(10000) //查询超时
                .serverSelectionTimeout(60000) //连接超时
                .connectTimeout(60000)
                .build();
    }

}
