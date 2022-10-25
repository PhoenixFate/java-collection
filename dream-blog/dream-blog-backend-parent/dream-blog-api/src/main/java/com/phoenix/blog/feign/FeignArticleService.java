package com.phoenix.blog.feign;

import com.phoenix.blog.common.constant.DreamBlogServerNameConstant;
import com.phoenix.blog.common.request.UserInfoRequest;
import com.phoenix.blog.entity.Label;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// value 指定目标微服务名字，path 目标微服务的上下路径contextPath值，如果目标微服务没有配置contextPath则不需要此path。
@FeignClient(value = DreamBlogServerNameConstant.DREAM_BLOG_ARTICLE, path = "/article")
public interface FeignArticleService {

    @GetMapping("/api/label/list/{ids}")
    List<Label> getLabelListByIds(@PathVariable("ids") List<String> labelIds);

    @ApiOperation("Feign接口-更新文章表和评论表中的用户信息")
    @PutMapping("/article/user")
    @ApiImplicitParam(name = "userInfoRequest", value = "用户信息对象", dataType = "UserInfoRequest", required = true)
    boolean updateUserInfo(@RequestBody UserInfoRequest userInfoRequest);

}
