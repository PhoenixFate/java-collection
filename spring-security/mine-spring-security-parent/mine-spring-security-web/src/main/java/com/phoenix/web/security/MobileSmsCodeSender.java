package com.phoenix.web.security;

import com.phoenix.core.authentication.mobile.SmsSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Auther: phoenix
 */
@Component
@Slf4j
public class MobileSmsCodeSender implements SmsSend {

    @Override
    public boolean sendSms(String mobile, String content) {
        log.info("Web应用新的短信验证码接口---向手机号" + mobile + "发送了验证码为：" + content);
        return true;
    }
}
