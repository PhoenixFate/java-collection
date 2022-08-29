package com.phoenix.core.authentication;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 当同一用户的session达到指定数量时候，所执行的策略，也就是会执行该类
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/29 15:05
 */
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        //1.获取用户名

    }

    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name","老王");
        map.put("id","195");

        String s = JSONObject.toJSONString(map);
        System.out.println(s);
    }

}
