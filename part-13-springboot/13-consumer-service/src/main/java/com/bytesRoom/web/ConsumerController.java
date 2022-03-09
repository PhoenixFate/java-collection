package com.bytesRoom.web;

import com.bytesRoom.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public User queryById(@PathVariable Integer id){
        String url="http://127.0.0.1:8091/user/"+id;
        User user=restTemplate.getForObject(url,User.class);
        return user;
    }



}
