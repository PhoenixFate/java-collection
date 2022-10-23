package com.phoenix.blog.oauth2.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.constant.DreamBlogServerNameConstant;
import com.phoenix.blog.common.constant.ResultEnum;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author phoenix
 * @Date 10/23/22 17:04
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class AuthService {

    //负载均衡客户端
    private final LoadBalancerClient loadBalancerClient;

    /**
     * 通过刷新令牌获取新的认证信息
     *
     * @param header       请求头：客户端信息 Basic clientId:clientSecret
     * @param refreshToken 刷新令牌
     * @return 自己把结果重新封装一下
     */
    public Result refreshToken(String header, String refreshToken) throws HttpProcessException {
        //采用客户端负载均衡，从nacos服务中获取对应服务的ip和端口号
        ServiceInstance serviceInstance = loadBalancerClient.choose(DreamBlogServerNameConstant.DREAM_BLOG_AUTH);
        if (serviceInstance == null) {
            return Result.error("未找到有效的认证服务器，请稍后重试");
        }
        //请求刷新令牌url
        String refreshTokenUrl = serviceInstance.getUri().toString() + "/auth/oauth/token";
        //封装刷新令牌请求参数
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "refresh_token");
        map.put("refresh_token", refreshToken);

        //构建配置请求头参数
        Header[] headers = HttpHeader.custom() //自定义请求头
                .contentType(HttpHeader.Headers.APP_FORM_URLENCODED) //数据类型
                .authorization(header) // 认证请求头（客户信息）
                .build();
        //请求配置
        HttpConfig httpConfig = HttpConfig.custom().headers(headers).url(refreshTokenUrl).map(map);
        //发送请求，响应认证信息
        String token = HttpClientUtil.post(httpConfig);
        JSONObject jsonObject = JSON.parseObject(token);
        //如果响应内容中包含了error属性值，则获取新的认证失败
        if (StringUtils.isNotEmpty(jsonObject.getString("error"))) {
            return Result.build(ResultEnum.TOKEN_PAST);
        }

        return Result.ok(jsonObject);
    }


}
