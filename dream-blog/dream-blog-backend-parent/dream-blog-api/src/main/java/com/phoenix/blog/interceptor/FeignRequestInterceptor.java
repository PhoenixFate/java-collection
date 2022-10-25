package com.phoenix.blog.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用feign进行远程调用的时候，先经过此拦截器，在此拦截器中将请求头带上访问令牌
 *
 * @Author phoenix
 * @Date 2022/10/25 13:59
 * @Version 1.0.0
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        //通过RequestContextHolder工具来获取请求相关变量
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            //获取请求对象
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isNotEmpty(authorization)) {
                //在使用feign远程调用的时候，请求头添加Authorization，也就是accessToken
                template.header(HttpHeaders.AUTHORIZATION, authorization);
            }
        }
    }
}
