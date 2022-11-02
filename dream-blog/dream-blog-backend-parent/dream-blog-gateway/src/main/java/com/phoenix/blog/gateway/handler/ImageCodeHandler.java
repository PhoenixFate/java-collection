package com.phoenix.blog.gateway.handler;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sun.security.util.SecurityConstants;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author phoenix
 * @Date 11/3/22 01:21
 * @Version 1.0
 */
@Slf4j
// @RequiredArgsConstructor
@Component
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {

    private static final Integer DEFAULT_IMAGE_WIDTH =100;

    private static final Integer DEFAULT_IMAGE_HEIGHT =40;

    private static final String DEFAULT_CODE_KEY="imageCode";

    private static final Integer CODE_TIME=60*30;

    @Resource
    private  RedisTemplate<String, String> redisTemplate;

    // private final  DefaultKaptcha defaultKaptcha;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        // 生成计算类型验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

        // 获取运算结果
        String result = captcha.text();
        // 保存验证码信息
        Optional<String> randomStr = serverRequest.queryParam("randomString");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        randomStr.ifPresent(s -> redisTemplate.opsForValue().set(DEFAULT_CODE_KEY + s, result,
                CODE_TIME, TimeUnit.SECONDS));

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.out(os);

        return ServerResponse.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
    }
}

