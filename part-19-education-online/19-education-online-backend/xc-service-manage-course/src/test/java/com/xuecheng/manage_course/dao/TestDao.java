package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeachPlanMapper teachPlanMapper;

    @Test
    public void testCourseBaseRepository() {
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper() {
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);
    }

    @Test
    public void testTeachPlanMapper() {
        TeachPlanNode teachPlanNode = teachPlanMapper.selectListByCourseId("4028e58161bcf7f40161bcf8b77c0000");
        System.out.println(teachPlanNode);

    }

    @Test
    public void testPageHelper() {
        //分页，查询第一页，每页显示10条记录
        PageHelper.startPage(1, 10);
        Page<CourseInfo> courseListWithPage = courseMapper.findCourseListWithPage();
        List<CourseInfo> result = courseListWithPage.getResult();
        System.out.println(result);
        long total = courseListWithPage.getTotal();
        System.out.println(total);
    }


}
