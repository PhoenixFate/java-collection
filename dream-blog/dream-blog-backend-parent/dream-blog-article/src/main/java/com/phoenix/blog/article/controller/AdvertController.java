package com.phoenix.blog.article.controller;


import com.phoenix.blog.article.request.AdvertRequest;
import com.phoenix.blog.article.service.IAdvertService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Advert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 广告信息表 前端控制器
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@RestController
@RequestMapping("/advert")
@AllArgsConstructor
@Api(tags = "广告")
public class AdvertController {

    private final IAdvertService advertService;

    /**
     * 带条件查询的广告分页列表
     *
     * @param advertRequest 带分页的广告查询对象
     * @return 广告列表
     */
    @PostMapping("/list")
    @ApiOperation("带条件查询的广告分页列表")
    @ApiImplicitParam(name = "advertRequest", value = "带分页的广告查询对象", dataType = "AdvertRequest", required = true)
    public Result page(@RequestBody AdvertRequest advertRequest) {
        return advertService.queryPage(advertRequest);
    }

    /**
     * 删除广告（同属删除OSS图片）
     *
     * @param id 广告id
     * @return 是否删除成功
     */
    @ApiOperation("删除广告")
    @DeleteMapping("/{id}")
    @ApiImplicitParam(name = "id", value = "广告id", required = true, dataType = "String")
    public Result delete(@PathVariable("id") String id) {
        return advertService.deleteById(id);
    }

    /**
     * 查询广告详情接口
     *
     * @param id 广告id
     * @return 是否删除成功
     */
    @GetMapping("/{id}")
    @ApiOperation("查询广告详情接口")
    @ApiImplicitParam(name = "id", value = "广告id", required = true, dataType = "String")
    public Result detail(@PathVariable("id") String id) {
        return Result.ok(advertService.getById(id));
    }

    /**
     * 修改广告信息接口
     *
     * @param advert 广告
     * @return 是否修改成功
     */
    @ApiOperation("修改广告信息接口")
    @PutMapping("/")
    public Result update(@RequestBody Advert advert) {
        advertService.updateById(advert);
        return Result.ok();
    }

    /**
     * 新增广告信息接口
     *
     * @param advert 广告信息
     * @return 是否新增成功
     */
    @ApiOperation("新增广告信息接口")
    @PostMapping("/")
    public Result save(@RequestBody Advert advert) {
        advertService.save(advert);
        return Result.ok();
    }

}
