package com.phoenix.blog.article.controller;

import com.phoenix.blog.article.service.ICommentService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Comment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评论信息表 前端控制器
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Api(tags = "评论")
@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

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

    /**
     * 通过评论id递归删除评论信息
     *
     * @param id 评论id
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @ApiOperation("通过评论id递归删除评论信息")
    @ApiImplicitParam(name = "id", value = "评论id", required = true, dataType = "String")
    public Result delete(@PathVariable("id") String id) {
        return commentService.deleteById(id);
    }

    /**
     * 新增评论信息接口
     *
     * @param comment 评论信息
     * @return 是否新增成功
     */
    @PostMapping("/")
    @ApiOperation("新增评论信息接口")
    public Result save(@RequestBody Comment comment) {
        commentService.save(comment);
        return Result.ok();
    }

}
