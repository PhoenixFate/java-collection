package com.phoenix.blog.article.api;

import com.phoenix.blog.article.service.ICommentService;
import com.phoenix.blog.common.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author phoenix
 * @Date 10/15/22 23:44
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
@Api(tags = "评论API接口", description = "不需要通过身份认证就可以直接访问")
public class ApiCommentController {

    private final ICommentService commentService;

    /**
     * 通过文章id递归查询所有评论
     *
     * @param articleId 文章id
     * @return 递归的评论列表
     */
    @GetMapping("/list/{articleId}")
    @ApiOperation("通过文章id查询所有评论")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String")
    public Result findByArticleId(@PathVariable("articleId") String articleId) {
        return commentService.findByArticleId(articleId);
    }

}
