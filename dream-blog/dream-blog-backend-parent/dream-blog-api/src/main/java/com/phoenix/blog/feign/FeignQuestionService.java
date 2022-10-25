package com.phoenix.blog.feign;

import com.phoenix.blog.common.constant.DreamBlogServerNameConstant;
import com.phoenix.blog.common.request.UserInfoRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = DreamBlogServerNameConstant.DREAM_BLOG_QUESTION, path = "/question")
public interface FeignQuestionService {

    @ApiOperation("Feign接口-更新问题表和回答表中的用户信息")
    @PutMapping("/question/user")
    @ApiImplicitParam(name = "userInfoRequest", value = "用户信息对象", dataType = "UserInfoRequest", required = true)
    boolean updateUserInfo(@RequestBody UserInfoRequest userInfoRequest);

}
