package com.phoenix.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class MainController {

    @RequestMapping({"/index", "/", ""})
    public String index(Map<String, Object> map) {
        //获取当前用户的认证信息：

        //方式一：
        //未认证的时候，Principal是username，认证之后，Principal是一个UserDetails实例
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            //已经认证，直接强转，获取用户名
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            map.put("username", username);
        }

        //方式二：
        //通过spring 自动注入的方式获取用户信息，参考  @RequestMapping("/user/info") 请求
        //直接注入Authentication 来获取用户信息

        //方式三：
        //通过spring 自动注入的方式获取用户信息，参考  @RequestMapping("/user/info2") 请求
        //直接注入UserDetails 来获取用户信息

        //返回resources/templates/index.html
        return "index";
    }


    /**
     * 方式二：
     * spring 会自动注入Authentication对象，通过Authentication获取用户信息
     *
     * @param authentication 认证详情
     * @return 用户信息
     */
    @RequestMapping("/user/info")
    @ResponseBody
    public Object userInfo(Authentication authentication) {
        return authentication.getPrincipal();
    }

    /**
     * 方式三
     * 通过@AuthenticationPrincipal注解，直接注入UserDetails对象
     * userDetails对象即用户信息对象
     *
     * @param userDetails 用户信息
     * @return 用户信息
     */
    @RequestMapping("/user/info2")
    @ResponseBody
    public Object getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

}
