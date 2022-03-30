package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface TeachPlanRepository extends JpaRepository<TeachPlan,String> {

    //根据courseId和parentId查询TeachPlan
    List<TeachPlan> findByCourseidAndParentid(String courseId,String parentId);

}
