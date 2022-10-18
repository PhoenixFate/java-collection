package com.phoenix.blog.question.api;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.question.service.IReplayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "回答管理API接口", description = "回答管理API接口, 不需要通过身份认证就可以访问")
@RestController
@RequestMapping("/api/replay")
@AllArgsConstructor
public class ApiReplayController {

    private final IReplayService replayService;

    /**
     * 通过问题ID递归所有的回答及子评论信息
     *
     * @param questionId 问题id
     * @return 某问题下的所有回复（递归查询）
     */
    @ApiImplicitParam(name = "questionId", value = "问题ID", required = true)
    @ApiOperation("通过问题ID递归所有的回答及子评论信息")
    @GetMapping("/list/{questionId}")
    public Result findByQuestionId(@PathVariable String questionId) {
        return replayService.findByQuestionId(questionId);
    }


}
