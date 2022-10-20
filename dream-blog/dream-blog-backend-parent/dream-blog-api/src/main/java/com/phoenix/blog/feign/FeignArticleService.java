package com.phoenix.blog.feign;

import com.phoenix.blog.common.request.UserInfoRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// value 指定目标微服务名字，path 目标微服务的上下路径contextPath值，如果目标微服务没有配置contextPath则不需要此path。
@FeignClient(value = "dream-blog-article", path = "/article")
public interface FeignArticleService {

    @ApiOperation("Feign接口-更新文章表和评论表中的用户信息")
    @PutMapping("/user")
    @ApiImplicitParam(name = "userInfoRequest", value = "用户信息对象", dataType = "UserInfoRequest", required = true)
    boolean updateUserInfo(@RequestBody UserInfoRequest userInfoRequest);

}
