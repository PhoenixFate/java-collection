package com.phoenix.core.authentication.mobile;

import com.phoenix.base.constant.CommonConstant;
import com.phoenix.core.authentication.exception.ValidationImageCodeException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验用户输入的手机验证码是否正确
 */
@Component
@AllArgsConstructor
public class MobileValidateFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //1.判断请求是否为手机登录，且为post请求
        if ("/mobile/form".equals(httpServletRequest.getRequestURI())
                && "post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            try {
                //登录校验验证码的合法性
                validateMobileCode(httpServletRequest);
            } catch (AuthenticationException exception) {
                //如果验证失败，则交由spring security 认证失败处理器来处理
                customAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);
                //结束当前请求，不继续放行
                return;
            }
        }
        //放行其他请求
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateMobileCode(HttpServletRequest httpServletRequest) {
        //先获取session中的验证码
        String mobileCode = (String) httpServletRequest.getSession().getAttribute(CommonConstant.SESSION_KEY_MOBILE_CODE);
        if (StringUtils.isBlank(mobileCode)) {
            throw new ValidationImageCodeException("登录请求异常");
        }
        //获取用户输入的验证码
        String inputImageCode = httpServletRequest.getParameter("code");
        //判断用户输入验证码和session中的验证码是否正确
        if (StringUtils.isBlank(inputImageCode)) {
            throw new ValidationImageCodeException("手机验证码不能为空");
        }
        if (!inputImageCode.equalsIgnoreCase(mobileCode)) {
            throw new ValidationImageCodeException("手机验证码错误");
        }
    }
}
