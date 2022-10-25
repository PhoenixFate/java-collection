package com.phoenix.blog.article.api;

import com.phoenix.blog.article.request.ArticleListRequest;
import com.phoenix.blog.article.service.IArticleService;
import com.phoenix.blog.common.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author phoenix
 * @Date 10/13/22 00:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/article")
@AllArgsConstructor
@Api(tags = "文章API接口", description = "不需要通过身份认证就可以直接访问")
public class ApiArticleController {

    private final IArticleService articleService;

    /**
     * 查询文章详情接口
     *
     * @param id 文章id
     * @return 文章详情
     */
    @GetMapping("/{id}")
    @ApiOperation("查询文章详情接口")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    public Result detail(@PathVariable("id") String id) {
        return articleService.findArticleAndLabelListById(id);
    }

    /**
     * 公开且已审核的文章列表接口
     * @param articleListRequest 文章列表查询接口
     * @return 文章列表
     */
    @PostMapping("/list")
    @ApiOperation("公开且已审核的文章列表接口")
    @ApiImplicitParam(name = "articleListRequest", value = "文章列表查询接口", dataType = "ArticleListRequest", required = true)
    public Result list(@RequestBody ArticleListRequest articleListRequest) {
        return articleService.findListByLabelIdOrCategoryId(articleListRequest);
    }

    /**
     * 更新文章浏览次数
     *
     * @param id 文章id
     * @return 是否更新成功
     */
    @ApiOperation("更新文章浏览次数")
    @PutMapping("/view/count/{id}")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    public Result updateViewCount(@PathVariable("id") String id) {
        return articleService.updateViewCount(id);
    }
}
