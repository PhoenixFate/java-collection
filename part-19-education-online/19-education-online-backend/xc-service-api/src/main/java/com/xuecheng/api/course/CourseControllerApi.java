package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.TeachPlan;
import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程管理接口", description = "课程管理的增删改查接口")
public interface CourseControllerApi {

    /**
     * 根据课程id，返回树形结构的TeachPlan
     *
     * @param courseId 课程id
     * @return 树形结构的TeachPlan
     */
    @ApiOperation("课程计划查询")
    TeachPlanNode findTeachPlanList(String courseId);

    @ApiOperation("课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult<CourseBase> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest);

    @ApiOperation("课程基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id", required = true, paramType = "path", dataType = "string")
    })
    CourseBase getCourseBaseById(String id);

    @ApiOperation("添加课程计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teachPlan", value = "课程计划", required = true, dataType = "TeachPlan")
    })
    ResponseResult addTeachPlan(TeachPlan teachPlan);

}
