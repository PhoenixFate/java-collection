package com.phoenix.core.mobile;

/**
 * 发送短信统一接口
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/12 15:58
 */
public interface SmsSend {

    /**
     * 发送短信
     *
     * @param mobile  手机号
     * @param content 发送的内容
     * @return 是否发送成功
     */
    boolean sendSms(String mobile, String content);

}
