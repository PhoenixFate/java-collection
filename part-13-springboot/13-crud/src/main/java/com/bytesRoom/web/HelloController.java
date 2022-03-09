package com.bytesRoom.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@Slf4j
@AllArgsConstructor
public class HelloController {

    private final DataSource dataSource;

    @GetMapping("hello")
    public String Hello(){
        log.info("------------------------ ---HelloController-------------hello-------------- ");
        System.out.println(dataSource);
        return "hello, spring boot";
    }

}
