package com.phoenix.web;


import com.phoenix.config.MyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;

//@RestController= @Controller+@ResponseBody
@RestController
// @AllArgsConstructor
public class HelloController {

    @Autowired
    MyHandler myhandler;
    @Autowired
    @Qualifier("MyDataSource")
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
