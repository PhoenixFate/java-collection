package com.xuecheng.api.category;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程分类管理api", description = "课程分类的常用查询接口", tags = {"课程分类管理接口"})
public interface CategoryControllerApi {

    @ApiOperation("查询分类")
    CategoryNode findList();
}
