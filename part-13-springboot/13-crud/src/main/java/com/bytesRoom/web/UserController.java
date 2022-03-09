package com.bytesRoom.web;

import com.bytesRoom.pojo.User;
import com.bytesRoom.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("user")
@AllArgsConstructor
public class UserController  {

    private final UserService userService;

    @GetMapping("{id}")
    @ResponseBody
    public User getUserById(@PathVariable("id") Integer id){
        return userService.findByQueryById(id);
    }

    @GetMapping("all")
    public String getAll(ModelMap model){
        // 查询用户
        List<User> users = this.userService.getAllUser();
        // 放入模型
        model.addAttribute("users", users);
        // 返回模板名称（就是classpath:/templates/目录下的html文件名）
        return "users";

    }
}
