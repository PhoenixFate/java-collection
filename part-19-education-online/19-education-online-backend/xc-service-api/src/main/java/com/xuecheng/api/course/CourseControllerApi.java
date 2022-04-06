package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.TeachPlan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "课程管理的增删改查接口", tags = {"课程管理接口"})
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
    QueryResponseResult<CourseInfo> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest);

    @ApiOperation("获取课程基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id", required = true, paramType = "path", dataType = "string")
    })
    CourseBase getCourseBaseById(String id);

    @ApiOperation("更新课程基础信息")
    ResponseResult updateCourseBase(String courseId, CourseBase courseBase);

    @ApiOperation("添加课程计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teachPlan", value = "课程计划", required = true, dataType = "TeachPlan")
    })
    ResponseResult addTeachPlan(TeachPlan teachPlan);

    @ApiOperation("获取课程营销信息")
    CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePicture(String courseId, String pictureId);

    @ApiOperation("查询当前课程的所有课程图片")
    CoursePic getCoursePictureList(String courseId);

    @ApiOperation("删除当前课程图片")
    ResponseResult deleteCoursePicture(String courseId);

    //根据id，返回课程详情所有内容
    @ApiOperation("查询课程视图")
    CourseView getCourseView(String courseId);

    @ApiOperation("课程预览")
    CoursePublishResult coursePreview(String courseId);

    @ApiOperation("课程发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "课程发布", required = true, paramType = "path", dataType = "string")
    })
    CoursePublishResult coursePublish(String courseId);
}
