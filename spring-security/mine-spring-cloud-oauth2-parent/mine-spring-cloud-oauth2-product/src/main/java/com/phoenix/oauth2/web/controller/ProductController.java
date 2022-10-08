package com.phoenix.oauth2.web.controller;

import com.phoenix.oauth2.base.result.RequestResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author phoenix
 * @Date 10/5/22 23:45
 * @Version 1.0
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public RequestResult list(Principal principal) {
        List<String> list = new ArrayList<>();
        list.add("眼镜");
        list.add("双肩包");
        list.add(principal.getName());
        return RequestResult.ok(list);
    }


}
