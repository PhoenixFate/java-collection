package com.phoenix.web.controller;

import com.phoenix.base.result.RequestResult;
import org.assertj.core.util.Lists;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户管理 web层
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/9/14 17:27
 */
@Controller
@RequestMapping("/user")
public class SysUserController {

    private static final String HTML_PREFIX = "system/user";

    @PreAuthorize("hasAuthority('sys:user')")
    @GetMapping(value = {"/", ""})
    public String user() {
        return HTML_PREFIX + "/user-list";
    }

    /**
     * 跳转到新增或者修改user页面
     *
     * @return 页面
     */
    @PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
    @GetMapping("/form")
    public String form() {
        return HTML_PREFIX + "/user-form.html";
    }

    //返回值的code等于200，则调用成功有权限，否则返回403
    @PostAuthorize("returnObject.code==200")
    @GetMapping("/delete/{id}")
    @ResponseBody
    public RequestResult deleteById(@PathVariable Long id) {
        if (id < 0) {
            return RequestResult.build(500, "id不能小于0", id);
        }
        return RequestResult.ok();
    }

    /**
     * 通过@PreFilter过滤请求参数
     * 过滤请求参数：filterTarget 指定哪个参数，filterObject 是集合中的每个元素
     *
     * @param ids ids
     * @return RequestResult
     */
    @PreFilter(filterTarget = "ids", value = "filterObject > 0") //对参数进行过滤
    @GetMapping("/delete/batch/{ids}") // /user/delete/batch/-1,0,1,2
    @ResponseBody
    public RequestResult deleteByIds(@PathVariable List<Long> ids) {
        return RequestResult.ok();
    }

    /**
     * 通过@PostFilter过滤返回结果
     * 过滤返回值：filterObject是返回值集合中的每一个元素，当表达式为true则对应元素会返回
     *
     * @return userList
     */
    @PostFilter("filterObject!=authentication.principal.username")
    @GetMapping("/temp/list")
    @ResponseBody
    public List<String> page() {
        return Lists.newArrayList("a", "b", "c", "d", "phoenix");
    }


}
