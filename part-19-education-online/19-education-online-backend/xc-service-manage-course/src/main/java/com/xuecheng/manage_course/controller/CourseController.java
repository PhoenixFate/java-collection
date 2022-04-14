package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.TeachPlan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

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
    @PreAuthorize("hasAuthority('course_find_list')")
    @GetMapping("/courseBase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") Integer page, @PathVariable("size") Integer size, CourseListRequest courseListRequest) {
        //调用工具类取出用户信息
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        courseListRequest.setCompanyId(userJwt.getCompanyId());

        return courseService.findCourseList(page, size, courseListRequest);
    }

    @Override
    @GetMapping("/courseBase/info/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseBaseById(courseId);
    }

    @Override
    @PutMapping("/courseBase/{courseId}")
    public ResponseResult updateCourseBase(@PathVariable("courseId") String courseId, @RequestBody CourseBase courseBase) {
        return courseService.updateCourseBase(courseId, courseBase);
    }

    @Override
    @PostMapping("/teachPlan/add")
    public ResponseResult addTeachPlan(@RequestBody TeachPlan teachPlan) {
        return courseService.addTeachPlan(teachPlan);
    }

    @Override
    @GetMapping("/courseMarket/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    @Override
    @PutMapping("/courseMarket/{courseId}")
    public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseId, @RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseId, courseMarket);
    }

    @Override
    @PostMapping("/coursePicture/add")
    public ResponseResult addCoursePicture(@RequestParam("courseId") String courseId, @RequestParam("pictureId") String pictureId) {
        return courseService.addPicture(courseId, pictureId);
    }

    @Override
    @GetMapping("/coursePicture/list/{courseId}")
    public CoursePic getCoursePictureList(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePictureList(courseId);
    }

    @Override
    @DeleteMapping("/coursePicture/delete/{courseId}")
    public ResponseResult deleteCoursePicture(@PathVariable("courseId") String courseId) {
        return courseService.deleteCoursePicture(courseId);
    }

    @Override
    @GetMapping("/courseView/{courseId}")
    public CourseView getCourseView(@PathVariable("courseId") String courseId) {
        return courseService.getCourseView(courseId);
    }

    @Override
    @PostMapping("/preview/{courseId}")
    public CoursePublishResult coursePreview(@PathVariable("courseId") String courseId) {
        return courseService.coursePreview(courseId);
    }

    @Override
    @PostMapping("/publish/{courseId}")
    public CoursePublishResult coursePublish(@PathVariable("courseId") String courseId) {
        return courseService.publishCourse(courseId);
    }
}
