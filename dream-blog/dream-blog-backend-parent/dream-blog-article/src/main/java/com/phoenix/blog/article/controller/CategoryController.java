package com.phoenix.blog.article.controller;

import com.phoenix.blog.article.request.CategoryRequest;
import com.phoenix.blog.article.service.ICategoryService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @ApiImplicitParam(name = "categoryRequest", value = "带分页的文章分类查询对象", dataType = "CategoryRequest", required = true)
    @PostMapping("/list")
    public Result page(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.queryPage(categoryRequest);
    }

    /**
     * 根据id查询详情信息
     *
     * @param id 分类id
     * @return 分类信息
     */
    @ApiOperation("根据id查询文章分类详情信息")
    @ApiImplicitParam(name = "id", value = "文章分类id", required = true, dataType = "String")
    @GetMapping("/{id}")
    public Result detail(@PathVariable("id") String id) {
        Category category = categoryService.getById(id);
        return Result.ok(category);
    }

    @ApiOperation("修改文章类别信息接口")
    @ApiImplicitParam(name = "category", value = "文章类别对象", dataType = "Category", required = true)
    @PutMapping()
    public Result update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.ok();
    }

    @ApiOperation("新增文章类别接口")
    @ApiImplicitParam(name = "category", value = "文章类别对象", dataType = "Category", required = true)
    @PostMapping
    public Result save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok();
    }

    @ApiOperation("删除文章分类接口")
    @ApiImplicitParam(name = "id", value = "文章类别id", dataType = "String", required = true)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        categoryService.removeById(id);
        return Result.ok();
    }

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
     */
    @ApiOperation("查询正常状态下的分类及分类下所有标签")
    @GetMapping("/label/list")
    public Result findCategoryAndLabelList() {
        return categoryService.findCategoryAndLabelList();
    }

}
