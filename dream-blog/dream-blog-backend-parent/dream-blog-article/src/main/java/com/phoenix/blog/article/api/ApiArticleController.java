package com.phoenix.blog.article.api;

import com.phoenix.blog.article.request.ArticleRequest;
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
    @ApiImplicitParam(name = "id", value = "文章id", readOnly = true, dataType = "String")
    public Result detail(@PathVariable("id") String id) {
        return articleService.findArticleAndLabelListById(id);
    }

}
