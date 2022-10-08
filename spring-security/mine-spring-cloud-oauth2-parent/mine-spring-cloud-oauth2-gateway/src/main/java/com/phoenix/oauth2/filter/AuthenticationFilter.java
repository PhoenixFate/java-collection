package com.phoenix.oauth2.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 在请求资源前，先通过此过滤器进行用户信息解析和校验转发
 *
 * @Author phoenix
 * @Date 2022/10/8 16:51
 * @Version 1.0.0
 */
@Component
@Slf4j
public class AuthenticationFilter extends ZuulFilter {

    /**
     * filterType：返回字符串代表过滤器的类型，返回值有：
     * pre：在请求路由之前执行
     * route：在请求路由时调用
     * post：请求路由之后调用， 也就是在route和error过滤器之后调用
     * error：处理请求发生错误时调用
     *
     * @return String
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 此方法返回整型数值，通过此数值来定义过滤器的执行顺序，数字越小优先级越高
     *
     * @return int 优先级
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 返回Boolean值，判断该过滤器是否执行。返回true表示要执行此过虑器，false不执行
     *
     * @return boolean 是否执行
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的业务逻辑
     *
     * @return Object
     * @throws ZuulException 异常
     */
    @Override
    public Object run() throws ZuulException {
        //通过Security上下文对象获得Authentication对象（未登录也会有Authentication对象，但不是Oauth2Authentication）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //如果解析到令牌就会封装到Oauth2Authentication对象
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        log.info("路由网关获取到的认证对象authentication: " + authentication);

        //principal:带了用户名，没有其他用户信息
        Object principal = authentication.getPrincipal();
        //获取所拥有的权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Set<String> authoritySet = AuthorityUtils.authorityListToSet(authorities);
        //请求描述
        Object details = authentication.getDetails();
        Map<String, Object> result = new HashMap<>();
        result.put("principal", principal);
        result.put("authorities", authoritySet);
        result.put("details", details);
        //获取当前请求上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //将用户信息和权限信息转成JSON，再通过base64进行编码
        String base64String = Base64Utils.encodeToString(JSON.toJSONString(result).getBytes());
        //添加自定义请求头
        currentContext.addZuulRequestHeader("auth-token", base64String);
        return null;
    }
}
