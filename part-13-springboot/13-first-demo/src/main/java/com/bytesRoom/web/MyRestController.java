package com.bytesRoom.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("rest")
public class MyRestController {

    @GetMapping("hello")
    public String Hello(){
        return "rest hello";
    }

}
