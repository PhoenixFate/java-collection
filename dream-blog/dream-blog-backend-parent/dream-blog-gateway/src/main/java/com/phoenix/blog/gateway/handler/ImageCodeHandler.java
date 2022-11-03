package com.phoenix.blog.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.phoenix.blog.gateway.filter.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    private static final Integer DEFAULT_IMAGE_WIDTH = 100;

    private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

    private static final String DEFAULT_CODE_KEY = "imageCode:";

    private static final Integer CODE_TIME = 60 * 30;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private DefaultKaptcha defaultKaptcha;

    @Resource
    private ObjectMapper objectMapper;
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        // 生成计算类型验证码
        //ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        // 获取运算结果
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);

        // 保存验证码信息
        Optional<String> randomStr = serverRequest.queryParam("randomString");

        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);

        if(randomStr.isPresent()){
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            randomStr.ifPresent(s -> redisTemplate.opsForValue().set(DEFAULT_CODE_KEY + s, text.toString(),
                    CODE_TIME, TimeUnit.SECONDS));

            // 转换流信息写出
            FastByteArrayOutputStream os = new FastByteArrayOutputStream();
            try {
                ImageIO.write(image, "jpeg", os);
            } catch (IOException e) {
                log.error("ImageIO write err", e);
                return Mono.error(e);
            }

            // 转换流信息写出
            //FastByteArrayOutputStream os = new FastByteArrayOutputStream();
            //captcha.out(os);
            //4.将验证码图片回写出去
            //ByteArrayOutputStream os = new ByteArrayOutputStream();
            //try {
            //    ImageIO.write(image, "jpg", os);
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
            //finally {
            //    try {
            //        os.close();
            //    } catch (IOException e) {
            //        e.printStackTrace();
            //    }
            //}

            return ServerResponse.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG)
                    .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
        }else {
            try {
                return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(Result.error("randomString 为空")));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

