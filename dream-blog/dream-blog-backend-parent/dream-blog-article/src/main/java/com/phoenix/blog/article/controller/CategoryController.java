package com.phoenix.blog.article.controller;

import com.phoenix.blog.article.request.CategoryRequest;
import com.phoenix.blog.article.service.ICategoryService;
import com.phoenix.blog.common.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章分类web层
 *
 * @Author phoenix
 * @Date 2022/10/11 15:22
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/category")
@AllArgsConstructor
@Api(tags = "文章分类")
public class CategoryController {

    private final ICategoryService categoryService;

    /**
     * 分页条件查询分页信息
     *
     * @param categoryRequest 条件查询（带分页条件）
     * @return 分页信息
     */
    @ApiOperation("根据分类名称与状态查询分类列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "categoryRequest", value = "带分页的文章分类查询对象", dataType = "CategoryRequest", required = true)
    })
    @PostMapping("/page")
    public Result page(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.queryPage(categoryRequest);
    }

}
