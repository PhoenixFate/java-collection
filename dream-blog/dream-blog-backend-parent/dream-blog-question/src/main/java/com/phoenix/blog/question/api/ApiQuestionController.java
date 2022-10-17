package com.phoenix.blog.question.api;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Question;
import com.phoenix.blog.question.service.IQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "问答管理接口", description = "问答管理接口,不用通过身份认证即可调用接口")
@RestController
@RequestMapping("/api/question")
@AllArgsConstructor
public class ApiQuestionController {

    private final IQuestionService questionService;

    @ApiOperation("分页查询热门问答列表接口")
    @PostMapping("/hot") // localhost:8002/question/api/question/hot
    public Result findHotList(@RequestBody BaseRequest<Question> req) {
        return questionService.findHotList(req);
    }

    @ApiOperation("分页查询最新问答列表接口")
    @PostMapping("/new")
    public Result findNewList(@RequestBody BaseRequest<Question> req) {
        return questionService.findNewList(req);
    }

    @ApiOperation("分页查询等待回答列表接口")
    @PostMapping("/wait")
    public Result findWaitList(@RequestBody BaseRequest<Question> req) {
        return questionService.findWaitList(req);
    }

    @ApiImplicitParam(name = "labelId", value = "标签ID", required = true)
    @ApiOperation("根据标签ID分页查询问答列表接口")
    @PostMapping("/list/{labelId}")
    public Result findListByLabelId(@RequestBody BaseRequest<Question> req,
                                    @PathVariable("labelId") String labelId) {
        return questionService.findListByLabelId(req, labelId);
    }

    @ApiOperation("查询问题详情接口")
    @ApiImplicitParam(name = "id", value = "问题ID", required = true)
    @GetMapping("/{id}")
    public Result view(@PathVariable("id") String id) {
        return questionService.findById(id);
    }


    @ApiOperation("更新浏览数接口")
    @ApiImplicitParam(name = "id", value = "问题ID", required = true)
    @PutMapping("/viewCount/{id}")
    public Result updateViewCount(@PathVariable("id") String id) {
        return questionService.updateViewCount(id);
    }

}
