package com.phoenix.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 一个配置，指定为springSecurity的配置，需要继承WebSecurityConfigurerAdapter
 */
@Configuration
@Slf4j
@EnableWebSecurity //开启spring security过滤器链 filter
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //BCryptPasswordEncoder: 密码加密器，根据密码的值，随机生成一个盐值，然后和密码一起加密
        return new BCryptPasswordEncoder();
    }


    /**
     * 认证管理器：
     * 1.认证信息（用户名、密码）
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //如果未指定，默认的用户名user，默认密码：在控制台中打印，需要调用super.configure(auth)
        //基于内存存储的认证信息
        //数据库存储的密码必须是加密后的，不然会报错java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
        String password = passwordEncoder().encode("123456");
        //BCryptPasswordEncoder每次加密的时候都有随机盐值，所以加密后的值每次不一样
        //$2a$10$Ihsl2kQiNdZLsj0jEBkkIezR1Ev2feKTExIVfxjO5ks4OZzBQXNjW
        //$2a$10$rfgA0UVsTcw5dFMnWAsmxueuq2WxI5bL22kgYJW9X0vFiLYRyw0bO
        log.info("加密后的密码：" + password);
        auth.inMemoryAuthentication().withUser("phoenix")
                .password(password)
                .authorities("admin");
    }

    /**
     * 资源权限配置
     * 1.被拦截的资源
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //采用httpBasic认证方式
        //http.httpBasic() //采用httpBasic认证方式
        //        .and()
        //        .authorizeRequests() //认证请求
        //        .anyRequest().authenticated(); //所有访问该应用的http请求都需要通过身份认证才可以访问

        //httpForm认证方式(表单登录方式)
        http.formLogin() //采用httpForm认证方式
                .and()
                .authorizeRequests() //认证请求
                .anyRequest().authenticated(); //所有访问该应用的http请求都需要通过身份认证才可以访问

    }
}
