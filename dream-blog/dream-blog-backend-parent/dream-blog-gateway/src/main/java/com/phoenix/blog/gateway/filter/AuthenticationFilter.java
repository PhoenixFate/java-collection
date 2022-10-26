package com.phoenix.blog.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 自定义认证过滤器，所有请求都经过此过滤器，判断请求头是否有Authentication
 *
 * @Author phoenix
 * @Date 10/26/22 19:38
 * @Version 1.0
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final String[] whitePath = {"/api/"};

    /**
     * 验证请求头是否带有Authentication
     *
     * @param exchange exchange
     * @param chain    过滤器链
     * @return Mono
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //请求对象
        ServerHttpRequest request = exchange.getRequest();
        //响应对象
        ServerHttpResponse response = exchange.getResponse();
        //请求路径
        String path = request.getPath().pathWithinApplication().value();

        //公开api接口进行放行，无需认证
        if (StringUtils.indexOfAny(path, whitePath) != -1) {
            //直接放行
            return chain.filter(exchange);
        }

        //获取认证请求头
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authentication)) {
            //没有带Authentication请求头信息，则响应错误信息
            //封装响应信息
            JSONObject message = new JSONObject();
            message.put("code", 1401);
            message.put("message", "缺少身份凭证");
            //响应消息转为字节
            byte[] bytes = message.toJSONString().getBytes(StandardCharsets.UTF_8);
            DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
            //设置响应对象状态码401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //设置响应对象内容并且指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
            //返回响应对象
            return response.writeWith(Mono.just(dataBuffer));
        }
        //有认证请求头，放行
        return chain.filter(exchange);
    }

    /**
     * 通过返回都整数来执行顺序，数字越小优先级越高
     *
     * @return 过滤器顺序
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
