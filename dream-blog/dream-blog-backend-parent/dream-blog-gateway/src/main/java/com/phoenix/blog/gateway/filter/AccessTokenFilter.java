package com.phoenix.blog.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JWSObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * 自定义AccessToken校验过滤器，需要在AuthenticationFilter执行之后执行：getOrder返回的值需要比AuthenticationFilter大
 *
 * @Author phoenix
 * @Date 10/26/22 21:45
 * @Version 1.0
 */
@Component
@Slf4j
@AllArgsConstructor
public class AccessTokenFilter implements GlobalFilter, Ordered {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 校验请求头中的令牌是否有效，查询redis中是否存在，不存在则jwt无效
     *
     * @param exchange ServerWebExchange
     * @param chain    过滤器链
     * @return Mono
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //请求对象
        ServerHttpRequest request = exchange.getRequest();
        //响应对象
        ServerHttpResponse response = exchange.getResponse();

        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        String token = StringUtils.substringAfter(authentication, "Bearer ");
        if (StringUtils.isEmpty(token)) {
            //如果为空，则为之前放行的请求，不需要认证
            return chain.filter(exchange);
        }
        //响应错误信息
        String message = null;
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JSONObject jsonObject = jwsObject.getPayload().toJSONObject();
            if (jsonObject == null) {
                log.info("错误的令牌 {}", token);
                message = "您的令牌已失效，请重新认证！";
            } else {
                String jti = jsonObject.get("jti").toString();
                String userInfoString = jsonObject.get("userInfo").toString();
                com.alibaba.fastjson.JSONObject userInfo = JSON.parseObject(userInfoString);

                //校验redis中是否存在对应jti的token
                String redisJtiValue = redisTemplate.opsForValue().get("dream-blog-oauth2:accessToken:" + userInfo.getString("username") + ":" + jti);
                if (redisJtiValue == null) {
                    log.info("令牌已过期 {}", token);
                    message = "您的令牌已过期，请重新认证！";
                }
            }
        } catch (ParseException e) {
            log.error("解析令牌失败 {}; {}", e.getMessage(), token);
            throw new RuntimeException(e);
        }
        if (message == null) {
            //令牌校验成功
            return chain.filter(exchange);
        }
        //令牌校验失败
        //封装响应信息
        JSONObject result = new JSONObject();
        result.put("code", 1401);
        result.put("message", message);
        //响应消息转为字节
        byte[] bytes = result.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        //设置响应对象状态码401
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //设置响应对象内容并且指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        //返回响应对象
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        //该过滤器需要在AuthenticationFilter之后执行
        return 1;
    }
}
