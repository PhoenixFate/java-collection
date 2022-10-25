package com.phoenix.blog.feign;

import com.phoenix.blog.common.constant.DreamBlogServerNameConstant;
import com.phoenix.blog.entity.SysMenu;
import com.phoenix.blog.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// value 指定目标微服务名字，path 目标微服务的上下路径contextPath值，如果目标微服务没有配置contextPath则不需要此path。
@FeignClient(value = DreamBlogServerNameConstant.DREAM_BLOG_SYSTEM, path = "/system")
public interface FeignSystemService {

    @GetMapping("/api/user/{username}")
    SysUser findUserByUsername(@PathVariable("username") String username);

    @GetMapping("/api/user/menu/list/{userId}")
    List<SysMenu> findMenuListByUserId(@PathVariable("userId") String userId);
}
