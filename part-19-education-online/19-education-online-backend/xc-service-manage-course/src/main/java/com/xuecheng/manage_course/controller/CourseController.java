package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.TeachPlan;
import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    /**
     * 根据课程id，返回树形结构的TeachPlan
     *
     * @param courseId 课程id
     * @return 树形结构的TeachPlan
     */
    @Override
    @GetMapping("/teachPlan/list/{courseId}")
    public TeachPlanNode findTeachPlanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachPlanPlan(courseId);
    }

    @Override
    @GetMapping("/courseBase/list/{page}/{size}")
    public QueryResponseResult<CourseBase> findCourseList(@PathVariable("page") Integer page, @PathVariable("size") Integer size, CourseListRequest courseListRequest) {
        return courseService.findCourseList(page, size, courseListRequest);
    }

    @Override
    @GetMapping("/courseBase/info/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable String courseId) {
        return courseService.getCourseBaseById(courseId);
    }

    @Override
    @PostMapping("/teachPlan/add")
    public ResponseResult addTeachPlan(@RequestBody TeachPlan teachPlan) {
        return courseService.addTeachPlan(teachPlan);
    }
}
