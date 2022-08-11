package com.phoenix.core.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/11 15:33
 */
@Configuration
public class KaptchaImageCodeConfig {

    /**
     * kaptcha.border 图片边框，合法值：yes , no yes
     * kaptcha.border.color 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue. black
     * kaptcha.border.thickness 边框厚度，合法值：>0 1
     * kaptcha.image.width 图片宽 200
     * kaptcha.image.height 图片高 50
     * kaptcha.producer.impl 图片实现类 com.google.code.kaptcha.impl.DefaultKaptcha
     * kaptcha.textproducer.impl 文本实现类 com.google.code.kaptcha.text.impl.DefaultTextCreator
     * kaptcha.textproducer.char.string 文本集合，验证码值从此集合中获取 abcde2345678gfynmnpwx
     * kaptcha.textproducer.char.length 验证码长度 5
     * kaptcha.textproducer.font.names 字体 Arial, Courier
     * kaptcha.textproducer.font.size 字体大小 40px.
     * kaptcha.textproducer.font.color 字体颜色，合法值： r,g,b 或者 white,black,blue. black
     * kaptcha.textproducer.char.space 文字间隔 2
     * kaptcha.noise.impl 干扰实现类 com.google.code.kaptcha.impl.DefaultNoise
     * kaptcha.noise.color 干扰线颜色，合法值： r,g,b 或者 white,black,blue. black
     * kaptcha.obscurificator.impl
     * 图片样式： 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼
     * com.google.code.kaptcha.impl.FishEyeGimpy 阴影
     * com.google.code.kaptcha.impl.ShadowGimpy
     * com.google.code.kaptcha.impl.WaterRipple
     * kaptcha.background.impl 背景实现类 com.google.code.kaptcha.impl.DefaultBackground
     * kaptcha.background.clear.from 背景颜色渐变，开始颜色 light grey
     * kaptcha.background.clear.to 背景颜色渐变， 结束颜色 white
     * kaptcha.word.impl 文字渲染器 com.google.code.kaptcha.text.impl.DefaultWordRenderer
     * kaptcha.session.key session key KAPTCHA_SESSION_KEY
     * kaptcha.session.date session date KAPTCHA_SESSION_DATE
     *
     * @return DefaultKaptcha
     */
    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_BORDER, "yes");
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "192,192,192");
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "110");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "36");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "28");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // 图片效果
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }


}
