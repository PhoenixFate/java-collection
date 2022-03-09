package com.bytesRoom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "jdbc4")
public class JdbcProperties4 {

    String url;
    String driverClassName;
    String username;
    String password;
    User user;

    @Data
    static class User{
        String name;
        int age;
        List<String> language;
    }


}
