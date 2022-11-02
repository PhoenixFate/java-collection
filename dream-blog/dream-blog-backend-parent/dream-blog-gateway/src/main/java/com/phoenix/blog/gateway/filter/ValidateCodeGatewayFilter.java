package com.phoenix.blog.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharSequenceUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import reactor.core.publisher.Mono;
import sun.security.util.SecurityConstants;

/**
 * @Author phoenix
 * @Date 11/3/22 01:24
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory<Object> {

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private static final String DEFAULT_CODE_KEY = "imageCode";

    private static final Integer CODE_TIME = 60 * 30;

    /**
     * GatewayFilter是一个接口
     * Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);
     */
    private static final String[] whitePath = {"/auth/login/"};

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            // 验证是不是登录请求路径。这里登录路径为auth2 提供的/oauth/token
            // boolean isAuthToken = CharSequenceUtils.containsAnyIgnoreCase(request.getURI().getPath(),
            //         SecurityConstants.OAUTH_TOKEN_URL);
            // if (!isAuthToken) {
            //     return chain.filter(exchange);
            // }
            // 不是登录请求，直接向下执行其他的过滤器

            if (StringUtils.indexOfAny(request.getURI().getPath(), whitePath) != -1) {
                //直接放行
                return chain.filter(exchange);
            }


            // 判断是不是需要验证 验证码的客户端
            // boolean isIgnoreClient = configProperties.getIgnoreClients().contains(WebUtils.getClientId(request));
            try {
                // only oauth and the request not in ignore clients need check code.
                // if (!isIgnoreClient) {
                //     checkCode(request);
                // }
                checkCode(request);

            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                final String errMsg = e.getMessage();
                return response.writeWith(Mono.create(monoSink -> {
                    try {
                        byte[] bytes = objectMapper.writeValueAsBytes(Result.error(errMsg));
                        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

                        monoSink.success(dataBuffer);
                    } catch (JsonProcessingException jsonProcessingException) {
                        log.error("对象输出异常", jsonProcessingException);
                        monoSink.error(jsonProcessingException);
                    }
                }));
            }
            return chain.filter(exchange);
        };
    }

    /**
     * 校验code
     * ServerHttpRequest 请求参数
     * 获取参数的code,根据randomStr或者mobile字段，取redis存的值。并删除存的数据
     * 然后作比较
     */
    private void checkCode(ServerHttpRequest request) throws Exception {
        String code = request.getQueryParams().getFirst("code");
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("验证码不能为空22");
        }

        String randomStr = request.getQueryParams().getFirst("randomStr");
        if (StringUtils.isBlank(randomStr)) {
            randomStr = request.getQueryParams().getFirst("mobile");
        }

        String key = DEFAULT_CODE_KEY + randomStr;

        Object codeObj = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        if (!code.equals(codeObj)) {
            throw new RuntimeException("验证码不合法");
        }
    }

}

