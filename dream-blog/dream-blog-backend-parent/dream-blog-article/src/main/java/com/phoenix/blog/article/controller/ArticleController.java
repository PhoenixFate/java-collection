package com.phoenix.blog.article.controller;


import com.phoenix.blog.article.request.ArticleRequest;
import com.phoenix.blog.article.service.IArticleService;
import com.phoenix.blog.common.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章信息表 前端控制器
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@RestController
@RequestMapping("/article")
@AllArgsConstructor
@Api(tags = "文章")
public class ArticleController {

    private final IArticleService articleService;

    /**
     * 带条件查询的文章分页列表
     *
     * @param articleRequest 文章条件查询
     * @return 文章分页列表
     */
    @PostMapping("/page")
    @ApiOperation("带条件查询的文章分页列表")
    @ApiImplicitParam(name = "articleRequest", value = "带分页的文章查询对象", dataType = "ArticleRequest", required = true)
    public Result page(@RequestBody ArticleRequest articleRequest) {
        return articleService.queryPage(articleRequest);
    }

    /**
     * 查询文章详情接口
     *
     * @param id 文章id
     * @return 文章详情
     */
    @GetMapping("/{id}")
    @ApiOperation("查询文章详情接口")
    @ApiImplicitParam(name = "id", value = "文章id", readOnly = true, dataType = "String")
    public Result detail(@PathVariable("id") String id) {
        return articleService.findArticleAndLabelListById(id);
    }

}
