package com.phoenix.blog.article.controller;

import com.phoenix.blog.article.request.LabelRequest;
import com.phoenix.blog.article.service.ILabelService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Label;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Api(tags = "标签管理")
@RestController
@RequestMapping("/label")
@AllArgsConstructor
public class LabelController {

    private final ILabelService labelService;

    /**
     * 分页条件查询分页信息
     *
     * @param labelRequest 带分页的标签查询对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation("根据分类id与标签名称查询列表接口")
    @ApiImplicitParam(name = "labelRequest", value = "带分页的标签查询对象", dataType = "LabelRequest", required = true)
    public Result page(@RequestBody LabelRequest labelRequest) {
        return labelService.queryPage(labelRequest);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询标签详情接口")
    @ApiImplicitParam(name = "id", value = "标签id", dataType = "String", required = true)
    public Result detail(@PathVariable("id") String id) {
        Label label = labelService.getById(id);
        return Result.ok(label);
    }

    @PutMapping("/")
    @ApiOperation("修改标签信息接口")
    @ApiImplicitParam(name = "label", value = "标签信息对象", dataType = "Label", required = true)
    public Result update(@RequestBody Label label) {
        labelService.updateById(label);
        return Result.ok();
    }

    @PostMapping("/")
    @ApiOperation("新增标签信息接口")
    @ApiImplicitParam(name = "label", value = "标签信息对象", dataType = "Label", required = true)
    public Result save(@RequestBody Label label) {
        labelService.save(label);
        return Result.ok();
    }

    @DeleteMapping("{id}")
    @ApiOperation("根据id删除标签信息接口")
    @ApiImplicitParam(name = "id", value = "标签id", dataType = "String", required = true)
    public Result delete(@PathVariable("id") String id) {
        labelService.removeById(id);
        return Result.ok();
    }


}