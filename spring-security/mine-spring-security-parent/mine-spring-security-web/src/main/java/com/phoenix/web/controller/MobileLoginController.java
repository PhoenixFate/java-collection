package com.phoenix.web.controller;

import com.phoenix.base.constant.CommonConstant;
import com.phoenix.base.result.RequestResult;
import com.phoenix.core.mobile.SmsSend;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 关于手机登录控制层
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/12 16:34
 */
@Controller
public class MobileLoginController {

    @Autowired
    private SmsSend smsSend;


    /**
     * 前往手机验证码登录页
     *
     * @return 页面路径
     */
    @GetMapping("/mobile/page")
    public String toMobilePage() {
        return "login-mobile";
    }

    @GetMapping("/code/mobile")
    public RequestResult sendMobileCode(HttpServletRequest request) {
        //1.生成一个手机验证码
        String mobileCode = RandomStringUtils.randomAlphanumeric(4);
        //2.将手机验证码保存到session中
        request.getSession().setAttribute(CommonConstant.SESSION_KEY_MOBILE_CODE, mobileCode);
        //3.发送验证码到用户手机上
        String mobile = request.getParameter("mobile");
        smsSend.sendSms(mobile, mobileCode);
        return RequestResult.ok();
    }


}
