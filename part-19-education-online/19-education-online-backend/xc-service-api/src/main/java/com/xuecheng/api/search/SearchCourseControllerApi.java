package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

/**
 * 搜索接口的api
 */
@Api(tags = {"课程搜索接口"})
public interface SearchCourseControllerApi {

    //搜索课程
    @ApiOperation("课程综合搜索列表")
    QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) throws Exception;


}
