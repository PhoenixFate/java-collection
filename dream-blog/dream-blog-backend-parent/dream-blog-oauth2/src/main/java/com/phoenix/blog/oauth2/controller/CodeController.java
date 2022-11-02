package com.phoenix.blog.oauth2.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.phoenix.blog.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@Slf4j
public class CodeController {

    @Resource
    private DefaultKaptcha defaultKaptcha;

    /**
     * 获取图形验证码
     */
    @RequestMapping("/code/image")
    public void imageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. 获取验证码字符串
        String imageText = defaultKaptcha.createText();
        log.info("---------------- 图形验证码：{}", imageText);
        //2. 字符串把它放到session中
        request.getSession().setAttribute(CommonConstant.SESSION_KEY_IMAGE_CODE, imageText);
        //3. 获取验证码图片
        BufferedImage bufferedImage = defaultKaptcha.createImage(imageText);

        //响应数据类型
        response.setContentType("image/png");

        //4.将验证码图片回写出去
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);

    }

}
