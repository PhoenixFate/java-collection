package com.phoenix.blog.article.api;

import com.phoenix.blog.article.service.ICategoryService;
import com.phoenix.blog.common.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 此控制层接口，不需要通过身份认证就可以调用
 *
 * @Author phoenix
 * @Date 10/12/22 23:03
 * @Version 1.0
 */
@Api(tags = "分类管理API接口", description = "不需要通过身份认证就可以直接访问")
@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class ApiCategoryController {

    private final ICategoryService categoryService;

    /**
     * 获取所有正常状态的文章分类接口
     *
     * @return 文章分类列表
     */
    @ApiOperation("获取所有正常状态的文章分类接口")
    @GetMapping("/list")
    public Result list() {
        return categoryService.finalAllNormal();
    }

    /**
     * 查询正常状态下的分类及分类下所有标签
     *
     * @return 带所属标签的分类列表
     */
    @ApiOperation("查询正常状态下的分类及分类下所有标签")
    @GetMapping("/label/list")
    public Result findCategoryAndLabelList() {
        return categoryService.findCategoryAndLabelList();
    }

}
