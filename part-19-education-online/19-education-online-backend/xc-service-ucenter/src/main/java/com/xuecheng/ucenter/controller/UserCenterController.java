package com.xuecheng.ucenter.controller;

import com.xuecheng.api.ucenter.UserCenterControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.XcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ucenter")
public class UserCenterController implements UserCenterControllerApi {

    @Autowired
    private XcUserService xcUserService;

    @Override
    @GetMapping("/user/extension")
    public XcUserExt getUserExtensionByUsername(@RequestParam("username") String username) {
        return xcUserService.getUserExtension(username);
    }
}
