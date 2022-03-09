package com.bytesRoom.config;


import lombok.Data;

@Data
public class JdbcProperties {

    String url;
    String driverClassName;
    String username;
    String password;

}
