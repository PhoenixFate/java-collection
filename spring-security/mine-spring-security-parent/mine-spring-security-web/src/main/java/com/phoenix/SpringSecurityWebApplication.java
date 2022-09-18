package com.phoenix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class SpringSecurityWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityWebApplication.class,args);
    }

    @Bean
    public Object testBean(PlatformTransactionManager platformTransactionManager){
        System.out.println(">>>>>>>>>>事务管理器：" + platformTransactionManager.getClass().getName());
        return new Object();
    }
}
