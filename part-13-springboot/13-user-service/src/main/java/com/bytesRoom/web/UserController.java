package com.bytesRoom.web;

import com.bytesRoom.pojo.User;
import com.bytesRoom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    public User getUserById(@PathVariable("id") Integer id){
//        try {
//            Thread.sleep(5000 );
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return userService.findByQueryById(id);
    }

    @PostMapping("/post")
    public User postUserById(@RequestParam(value="id",required = false) Integer id){
        System.out.println("--------id:  "+id);
//        try {
//            Thread.sleep(5000 );
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return userService.findByQueryById(id);
    }


}
