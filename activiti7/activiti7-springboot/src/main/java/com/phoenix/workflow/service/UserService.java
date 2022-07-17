package com.phoenix.workflow.service;

import org.springframework.stereotype.Service;

/**
 * 匹配 ${userService.getUsername()}
 */
@Service
public class UserService {

    /**
     * 返回办理人
     * @return 办理人
     */
    public String getUsername(){
        return "gu";
    }


}
