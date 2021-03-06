package com.bytesRoom.web;

import com.bytesRoom.config.MyHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

//@RestController= @Controller+@ResponseBody
@RestController
@AllArgsConstructor
public class HelloController {

    MyHandler myhandler;
    DataSource dataSource;

    @GetMapping("hello")
    // @ResponseBody
    public String hello(){
        return "hello";
    }

    @GetMapping("test")
    // @ResponseBody
    public  void test(){
        // myhandler.test();
        // return "test";
    }

}
