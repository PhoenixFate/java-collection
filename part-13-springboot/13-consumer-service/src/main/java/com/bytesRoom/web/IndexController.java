package com.bytesRoom.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println(session.getId());
        return "hello";
    }

}
