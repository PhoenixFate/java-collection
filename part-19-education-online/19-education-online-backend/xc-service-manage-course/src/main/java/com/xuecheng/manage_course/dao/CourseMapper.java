package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {
    CourseBase findCourseBaseById(String id);

    //课程列表, 根据companyId，来细分数据颗粒度
    Page<CourseInfo> findCourseListWithPage(CourseListRequest courseListRequest);

}
