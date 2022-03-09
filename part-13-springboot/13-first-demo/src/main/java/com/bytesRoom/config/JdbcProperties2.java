package com.bytesRoom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jdbc2")
public class JdbcProperties2 {

    String url;
    String driverClassName;
    String username;
    String password;

}
