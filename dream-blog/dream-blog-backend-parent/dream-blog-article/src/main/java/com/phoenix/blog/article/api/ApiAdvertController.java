package com.phoenix.blog.article.api;

import com.phoenix.blog.article.service.IAdvertService;
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
@RequestMapping("/api/advert")
@AllArgsConstructor
@Api(tags = "广告API接口", description = "不需要通过身份认证就可以直接访问")
public class ApiAdvertController {

    private final IAdvertService advertService;

    /**
     * 通过广告位置查询广告列表
     *
     * @param position 广告位置
     * @return 广告列表
     */
    @GetMapping("/list/{position}")
    @ApiOperation("通过广告位置查询广告列表")
    @ApiImplicitParam(name = "position", value = "广告位置", required = true, dataType = "String")
    public Result listByPosition(@PathVariable("position") Integer position) {
        return advertService.findByPosition(position);
    }

}
