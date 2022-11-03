package com.phoenix.blog.oauth2.filter;

import com.phoenix.blog.common.constant.CommonConstant;
import com.phoenix.blog.oauth2.handler.CustomAuthenticationFailureHandler;
import com.phoenix.blog.oauth2.property.SpringSecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OncePerRequestFilter: 所有请求之前被调用一次
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/11 17:04
 */
@Component("imageCodeValidateFilter")
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private SpringSecurityProperties springSecurityProperties;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //1.只拦截登录处理请求
        // 如果是登录处理请求，则校验输入的验证码是否正确 并且需要是post请求
        //if (springSecurityProperties.getAuthentication().getLoginProcessingUrl()
        //        .equalsIgnoreCase(httpServletRequest.getRequestURI()) && httpServletRequest.getMethod().equalsIgnoreCase("post")
        //) {
        //    try {
        //        //登录校验验证码的合法性
        //        validateImageCode(httpServletRequest);
        //    } catch (ValidationImageCodeException e) {
        //        //如果验证失败，则交由spring security 认证失败处理器来处理
        //        customAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
        //        //结束当前请求，不继续放行
        //        return;
        //    }
        //}
        //放行其他请求
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateImageCode(HttpServletRequest httpServletRequest) {
        //先获取session中的验证码
        String imageCode = (String) httpServletRequest.getSession().getAttribute(CommonConstant.SESSION_KEY_IMAGE_CODE);
        if (StringUtils.isBlank(imageCode)) {
            throw new ValidationImageCodeException("登录请求异常");
        }
        //获取用户输入的验证码
        String inputImageCode = httpServletRequest.getParameter("code");
        //判断用户输入验证码和session中的验证码是否正确
        if (StringUtils.isBlank(inputImageCode)) {
            throw new ValidationImageCodeException("验证码不能为空");
        }
        if (!inputImageCode.equalsIgnoreCase(imageCode)) {
            throw new ValidationImageCodeException("验证码错误");
        }
    }


}
