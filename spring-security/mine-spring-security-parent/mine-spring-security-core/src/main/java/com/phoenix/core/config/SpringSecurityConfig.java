package com.phoenix.core.config;

import com.phoenix.core.filter.ImageCodeValidateFilter;
import com.phoenix.core.mobile.MobileAuthenticationConfig;
import com.phoenix.core.mobile.MobileValidateFilter;
import com.phoenix.core.property.SpringSecurityProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;

/**
 * 一个配置，指定为springSecurity的配置，需要继承WebSecurityConfigurerAdapter
 */
@Configuration
@Slf4j
@EnableWebSecurity //开启spring security过滤器链 filter
@AllArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //BCryptPasswordEncoder: 密码加密器，根据密码的值，随机生成一个盐值，然后和密码一起加密
        return new BCryptPasswordEncoder();
    }

    //从application.yml中读取配置信息
    private SpringSecurityProperties springSecurityProperties;
    //自定义的userDetailsService
    private UserDetailsService customUserDetailsService;
    //spring security 认证成功处理器
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    //spring security 认证失败处理器
    private AuthenticationFailureHandler customAuthenticationFailureHandler;
    //验证码校验过滤器
    private ImageCodeValidateFilter imageCodeValidateFilter;

    //手机登录，手机验证码过滤器: 校验手机验证码
    private MobileValidateFilter mobileValidateFilter;
    //手机号认证配置：校验手机号是否存在，就是手机号认证
    private MobileAuthenticationConfig mobileAuthenticationConfig;
    /**
     * 自定义session失效的返回策略:
     * 1.可以重定向到登录页面
     * 2.也可以返回json数据
     */
    private InvalidSessionStrategy customInvalidSessionStrategy;
    /**
     * 用户在系统中的session数量超过最大数所执行的策略
     */
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    /**
     * 认证管理器：
     * 1.认证信息（用户名、密码）
     *
     * @param auth AuthenticationManagerBuilder
     * @throws Exception e
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //如果未指定，默认的用户名user，默认密码：在控制台中打印，需要调用super.configure(auth)
        //1.0 基于内存存储的认证信息 ---------start
        //数据库存储的密码必须是加密后的，不然会报错java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
        // String password = passwordEncoder().encode("123456");
        // //BCryptPasswordEncoder每次加密的时候都有随机盐值，所以加密后的值每次不一样
        // //$2a$10$Ihsl2kQiNdZLsj0jEBkkIezR1Ev2feKTExIVfxjO5ks4OZzBQXNjW
        // //$2a$10$rfgA0UVsTcw5dFMnWAsmxueuq2WxI5bL22kgYJW9X0vFiLYRyw0bO
        // log.info("加密后的密码：" + password);
        // auth.inMemoryAuthentication().withUser("phoenix")
        //         .password(password)
        //         .authorities("admin");
        //基于内存存储的认证信息 ---------end

        //2.0 基于UserDetailsService的实现
        auth.userDetailsService(customUserDetailsService);
    }

    private DataSource dataSource;

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //是否启动项目的时候自动创建表，true为自动创建表
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


    /**
     * 当认证成功之后，SpringSecurity会重定向到上一次的请求路径上
     * 资源权限配置
     * 1.被拦截的资源
     *
     * @param http HttpSecurity
     * @throws Exception e
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //采用httpBasic认证方式
        //http.httpBasic() //采用httpBasic认证方式
        //        .and()
        //        .authorizeRequests() //认证请求
        //        .anyRequest().authenticated(); //所有访问该应用的http请求都需要通过身份认证才可以访问

        //httpForm认证方式(表单登录方式)(默认就是表单登录方式)
        http.addFilterBefore(mobileValidateFilter, UsernamePasswordAuthenticationFilter.class) //将手机验证码过滤器添加到用户名密码过滤器之前
                .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class) //将验证码过滤器添加到用户名密码过滤器之前
                .formLogin() //采用httpForm认证方式
                .loginPage(springSecurityProperties.getAuthentication().getLoginPage()) //自定义登录页面
                .loginProcessingUrl(springSecurityProperties.getAuthentication().getLoginProcessingUrl()) //登录表单提交处理url，默认是/login
                .usernameParameter(springSecurityProperties.getAuthentication().getUsernameParameter())//登录用户名的参数名
                .passwordParameter(springSecurityProperties.getAuthentication().getPasswordParameter())//登录密码的参数名
                .successHandler(customAuthenticationSuccessHandler) //认证成功处理器
                .failureHandler(customAuthenticationFailureHandler) //认证失败处理器
                .and()
                .authorizeRequests() //认证请求
                .antMatchers(springSecurityProperties.getAuthentication().getLoginPage(),
                        springSecurityProperties.getAuthentication().getImageCodeUrl(),
                        springSecurityProperties.getAuthentication().getMobileCodeUrl(),
                        springSecurityProperties.getAuthentication().getMobilePage()
                ).permitAll() //放行/login/page 不需要认证访问
                .anyRequest().authenticated() //所有访问该应用的http请求都需要通过身份认证才可以访问
                .and()
                .rememberMe() //开启rememberMe
                .tokenRepository(jdbcTokenRepository()) //使用jdbcTokenRepository保存登录信息
                .tokenValiditySeconds(60 * 60 * 24 * 7) //rememberMe的有效时长：7天
                .and()
                .sessionManagement()//session管理
                .invalidSessionStrategy(customInvalidSessionStrategy) //当session失效后的处理类
                .maximumSessions(1) //每个用户在系统中最多有多少个session
                .expiredSessionStrategy(sessionInformationExpiredStrategy)//用户在系统中的session数量超过最大数所执行的策略
                .maxSessionsPreventsLogin(true) //当一个用户达到最大session数量，则不允许后面再登录
        ;

        //将手机认证添加到过滤器链上
        http.apply(mobileAuthenticationConfig);

    }

    /**
     * 针对静态资源进行放行
     *
     * @param web web
     */
    @Override
    public void configure(WebSecurity web) {
        // /*表示儿子目录 /**表示所有子目录
        web.ignoring().antMatchers(springSecurityProperties.getAuthentication().getStaticPath());
    }
}
