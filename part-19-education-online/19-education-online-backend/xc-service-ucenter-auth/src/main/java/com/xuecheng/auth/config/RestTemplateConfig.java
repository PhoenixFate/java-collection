package com.xuecheng.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        okHttp3ClientHttpRequestFactory.setConnectTimeout(30 * 1000);
        okHttp3ClientHttpRequestFactory.setWriteTimeout(30 * 1000);
        okHttp3ClientHttpRequestFactory.setReadTimeout(30 * 1000);
        return new RestTemplate();
    }

}
