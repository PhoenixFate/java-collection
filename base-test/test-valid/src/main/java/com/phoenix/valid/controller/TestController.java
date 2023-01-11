package com.phoenix.valid.controller;

import com.phoenix.valid.domain.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @Author phoenix
 * @Date 1/7/23 00:02
 * @Version 1.0
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/add1")
    @ResponseBody
    public String add1(@Valid Employee employee) {
        return "新增员工成功1";
    }

    @RequestMapping("/add2")
    @ResponseBody
    public String add2(@Valid Employee employee, BindingResult bindingResult) {
        return "新增员工成功2";
    }

    @RequestMapping("/add3")
    @ResponseBody
    public String add3(@Valid Employee employee, BindingResult bindingResult) {
        // 所有字典是否校验通过，true-数据有误，false-数据无误
        if (bindingResult.hasErrors()) {
            // 验证有误情况，返回第一天错误信息到前端
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return "新增员工成功4";
    }

    @RequestMapping("/add4")
    @ResponseBody
    public String add4(@Validated Employee employee) {
        return "新增员工成功4";
    }

    @PostMapping("/add5")
    @ResponseBody
    public String add5(@Valid @RequestBody Employee employee) {
        return "新增员工成功5";
    }
}
