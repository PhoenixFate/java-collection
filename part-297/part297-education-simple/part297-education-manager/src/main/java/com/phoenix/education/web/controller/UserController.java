package com.phoenix.education.web.controller;

import com.phoenix.education.model.User;
import com.phoenix.education.service.IUserService;
import com.phoenix.education.web.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController extends BaseController<User> {

    @Autowired
    private IUserService userService;

    @RequestMapping("login")
    public String login(){
        System.out.println("hello login");
        return "Default";
    }

    @RequestMapping("find")
    public String find(Integer id){
        User user = userService.findById(id);
        System.out.println(user);
        return "Default";
    }


    @RequestMapping(MANAGE)
    public String manage(){

        return MANAGE_PAGE;
    }

    @RequestMapping(INFO)
    public String info(){
        return INFO_PAGE;
    }


    @RequestMapping(EDIT)
    public String edit(){
        return EDIT_PAGE;
    }

}
