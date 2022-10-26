package com.phoenix.blog.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * I18n配置文件
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {
    /**
     * LocaleResolver 是指用什么策略来检测请求是哪一种Local, Spring 提供以下几种策略：
     * <p>
     * 2.1、AcceptHeaderLocaleResolver
     * 根据浏览器Http Header中的accept-language域判定(accept-language域中一般包含了当前操作系统的语言设定，
     * 可通过HttpServletRequest.getLocale方法获得此域的内容)。 改变Local 是不支持的，
     * 即不能调用LocaleResolver接口的 setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale); 方法设置Local.
     * 2.2、SessionLocaleResolver
     * 根据用户本次会话过程中的语言设定决定语言种类（如：用户登录时选择语言种类，则此次登录周期内统一使用此语言设定）。
     * <p>
     * 2.3、CookieLocaleResolver
     * 根据Cookie判定用户的语言设定（Cookie中保存着用户前一次的语言设定参数）。
     * <p>
     * 2.4、FixedLocaleResolver 一直使用固定的Local, 改变Local 是不支持的 见(2.1)
     * <p>
     * DispatchServlet 将在初始化的时候， 会调用initLocaleResolver(context) 方法去配置文件中找名字为“localeResolver" bean.
     * 如果有就用配置文件配置的localResolver. 如果没有配置将用默认的localResolver "AcceptHeaderLocaleResolver".
     *
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        //设置默认区域
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
    }

    /**
     * LocaleChangeInterceptor 的使用：
     * 如果想要用户能改变Local, 我们需要配置 LocaleChangeInterceptor, 这个拦截器将检查传入的请求，
     * 如果请求中有“lang" 的参数(参数可以配置），如http://localhost:8080/test?lang=zh_CN. 该Interceptor将使用localResolver改变当前用户的Local
     * <p>
     * 这个对于前后端分离的其实没啥用了
     *
     * @return LocaleChangeInterceptor
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        // 参数名
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * 要使得LocaleChangeInterceptor 有效果
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}