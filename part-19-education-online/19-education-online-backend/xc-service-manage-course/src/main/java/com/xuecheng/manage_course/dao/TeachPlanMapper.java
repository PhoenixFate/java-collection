package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeachPlanMapper {

    //课程计划
    TeachPlanNode selectListByCourseId(String courseId);
}
