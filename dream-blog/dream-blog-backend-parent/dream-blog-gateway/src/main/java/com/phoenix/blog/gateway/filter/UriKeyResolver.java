package com.phoenix.blog.gateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关限流过滤器
 *
 * @Author phoenix
 * @Date 2022/10/26 14:57
 * @Version 1.0.0
 */
@Component
public class UriKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        //针对微服务的每个请求进行限流
        return Mono.just(exchange.getRequest().getURI().getPath());
    }
}
