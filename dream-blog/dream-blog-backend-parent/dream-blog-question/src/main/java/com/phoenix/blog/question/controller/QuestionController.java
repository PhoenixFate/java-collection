package com.phoenix.blog.question.controller;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.request.UserInfoRequest;
import com.phoenix.blog.entity.Question;
import com.phoenix.blog.question.req.QuestionUserRequest;
import com.phoenix.blog.question.service.IQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 问题信息表 前端控制器
 * </p>
 *
 * @author phoenix
 */
@Api(tags = "问答管理接口")
@RestController
@RequestMapping("/question")
@AllArgsConstructor
public class QuestionController {

    private final IQuestionService questionService;

    @ApiOperation("修改问题信息接口")
    @PutMapping // put 方式请求 /question/question
    public Result update(@RequestBody Question question) {
        return questionService.updateOrSave(question);
    }

    @ApiOperation("新增问题信息接口")
    @PostMapping // post 请求 /question/question
    public Result save(@RequestBody Question question) {
        return questionService.updateOrSave(question);
    }

    @ApiOperation("删除问题信息接口")
    @ApiImplicitParam(name = "id", value = "问题ID", required = true)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        return questionService.deleteById(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "问题ID", required = true),
            @ApiImplicitParam(name = "count", value = "点赞数(只能是1或-1)", required = true)
    })
    @ApiOperation("更新点赞数")
    @PutMapping("/likes/{id}/{count}")
    public Result updateLikesNumber(@PathVariable("id") String id,
                                    @PathVariable("count") int count) {
        return questionService.updateLikesNumber(id, count);
    }

    @ApiOperation("根据用户id查询问题列表")
    @PostMapping("/user")
    public Result findListByUserId(@RequestBody QuestionUserRequest req) {
        return questionService.findListByUserId(req);
    }

    @ApiOperation("查询提问总记录")
    @GetMapping("/total")  // /question/question/total
    public Result questionTotal() {
        return questionService.getQuestionTotal();
    }

    @ApiOperation("Feign接口-更新问题表和回答表中的用户信息")
    @PutMapping("/user")
    @ApiImplicitParam(name = "userInfoRequest", value = "用户信息对象", dataType = "UserInfoRequest", required = true)
    public boolean updateUserInfo(@RequestBody UserInfoRequest userInfoRequest) {
        return questionService.updateUserInfo(userInfoRequest);
    }

}
