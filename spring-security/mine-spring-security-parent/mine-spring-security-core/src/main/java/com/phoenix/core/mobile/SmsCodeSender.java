package com.phoenix.core.mobile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 发送短信验证码，第三方的短信服务接口
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/12 16:00
 */
@Slf4j
//@Component
public class SmsCodeSender implements SmsSend {

    /**
     * @param mobile  手机号
     * @param content 发送的内容
     * @return 是否发送成功
     */
    @Override
    public boolean sendSms(String mobile, String content) {
        String sendContent = String.format("【字节空间】, 你的登录验证码为 %s, 请勿泄露给别人", content);
        //调用第三方的sdk发送短信
        log.info("----------------- 向手机号：{} 发送短信，短信内容为: {}", mobile, sendContent);
        return true;
    }
}
