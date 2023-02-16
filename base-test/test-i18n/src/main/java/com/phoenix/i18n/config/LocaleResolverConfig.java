package com.phoenix.i18n.config;

/**
 * @Author phoenix
 * @Date 2023/2/16 10:40
 * @Version 1.0.0
 */
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleResolverConfig {
    private static Logger log = LoggerFactory.getLogger(LocaleResolverConfig.class);

    /**
     * 解析器(只做数据的解析操作，但需要主动调用)
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        log.info("LocaleResolver success");
        return new LanguageLocaleResolverConfig();
    }

    /**
     * 拦截器，针对请求过来时，
     * @return
     */
    //@Bean
    //public WebMvcConfigurer localeInterceptor() {
    //    return new WebMvcConfigurer() {
    //        @Override
    //        public void addInterceptors(InterceptorRegistry registry) {
    //            LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
    //            localeInterceptor.setParamName("l");
    //            //针对指定的请求做过滤操作、增加某些拦截操作
    //            registry.addInterceptor(localeInterceptor);
    //        }
    //    };
    //}
}

class LanguageLocaleResolverConfig implements LocaleResolver {
    private static Logger log = LoggerFactory.getLogger(LanguageLocaleResolverConfig.class);

    private static final String PATH_PARAMETER_SPLIT = "_";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 获取请求来的语言方式
        String language = request.getParameter("l");
        log.info("language  l:{}", language);
        // 获取请求头默认的local对象
        Locale locale = request.getLocale();
        log.info("Add default.");
        if (!StringUtils.isEmpty(language)) {
            // 按照指定的正则表达式，解析出相关的数据信息
            String[] split = language.split(PATH_PARAMETER_SPLIT);
            // 解析出数据后，修改local对象
            locale = new Locale(split[0], split[1]);
            log.info("Add custom.");
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // TODO Auto-generated method stub

    }

}
