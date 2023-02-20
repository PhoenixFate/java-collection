package com.phoenix.i18n.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @Author phoenix
 * @Date 2023/2/16 10:32
 * @Version 1.0.0
 */
@RestController
public class TestController {


    private Logger log = LoggerFactory.getLogger(TestController.class);

    private static final String PATH_PARAMETER_SPLIT = "_";

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key, HttpServletRequest request, String... strings ) {
        String language = request.getParameter("language");
        log.info("language:{}",language);
        //获取请求头默认的local对象
        Locale locale = request.getLocale();
        log.info("Add default.");
        if(!StringUtils.isEmpty(language)) {
            //按照指定的正则表达式，解析出相关的数据信息
            String[] split = language.split(PATH_PARAMETER_SPLIT);
            //解析出数据后，修改local对象
            locale = new Locale(split[0], split[1]);
            log.info("Add custom.");
        }
        return this.messageSource.getMessage(key, strings,locale);
    }

    /**
     * en_US <br>
     * zh_CN
     * @param request
     * @return
     */
    @RequestMapping("/test1")
    public String test1(HttpServletRequest request) {
        //log.info("local:{}",Locale.getDefault().getLanguage());
        return getMessage("test.name",request,null);
    }

    /**
     * en_US <br>
     * zh_CN
     * @param request
     * @return
     */
    @RequestMapping("/test2")
    public String test2(HttpServletRequest request) {
        //log.info("local:{}",Locale.getDefault().getLanguage());
        String name = request.getParameter("name");

        return getMessage2("test.name",name);
    }

    public String getMessage2(String key,String... strings ) {
        return this.messageSource.getMessage(key, strings, LocaleContextHolder.getLocale());
    }

}