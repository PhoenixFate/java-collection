package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.TeachPlan;
import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private TeachPlanMapper teachPlanMapper;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private TeachPlanRepository teachPlanRepository;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CoursePictureRepository coursePictureRepository;


    //课程计划查询
    public TeachPlanNode findTeachPlanPlan(String courseId) {
        return teachPlanMapper.selectListByCourseId(courseId);
    }

    public QueryResponseResult<CourseBase> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //条件值对象
        CourseBase courseBase = new CourseBase();
        //设置条件值
        if (StringUtils.isNoneEmpty(courseListRequest.getCompanyId())) {
            courseBase.setCompanyId(courseListRequest.getCompanyId());
        }
        //定义条件对象
        Example<CourseBase> example = Example.of(courseBase, exampleMatcher);

        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseBase> all = courseBaseRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询

        QueryResult<CourseBase> queryResult = new QueryResult<CourseBase>();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }

    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult addTeachPlan(TeachPlan teachPlan) {
        if (teachPlan == null || StringUtils.isEmpty(teachPlan.getCourseid()) || StringUtils.isEmpty(teachPlan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程计划
        String courseId = teachPlan.getCourseid();
        //如果parentId为空，则把一级的id设置为parentId
        String parentId = teachPlan.getParentid();
        if (StringUtils.isEmpty(parentId)) {
            //如果父节点为空则获得根节点
            parentId = this.getTeachPlanRoot(courseId);
        }
        //新节点
        TeachPlan teachPlanNew = new TeachPlan();
        //将页面提交的teachPlan信息拷贝到new对象中
        BeanUtils.copyProperties(teachPlan, teachPlanNew);
        teachPlanNew.setParentid(parentId);
        teachPlanNew.setCourseid(courseId);
        //父节点
        Optional<TeachPlan> optional = teachPlanRepository.findById(parentId);
        TeachPlan parentNode = optional.get();
        //根据父节点的级别来设置
        if (parentNode.getGrade().equals("1")) {
            teachPlanNew.setGrade("2");
        } else {
            teachPlanNew.setGrade("3");
        }
        teachPlanRepository.save(teachPlanNew);
        //操作成功
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取根节点
     *
     * @param courseId 课程id
     * @return 根节点id
     */
    private String getTeachPlanRoot(String courseId) {
        //校验课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        //取出课程计划的根节点
        List<TeachPlan> teachPlanList = teachPlanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachPlanList == null || teachPlanList.size() == 0) {
            //新增一个根节点
            TeachPlan root = new TeachPlan();
            root.setCourseid(courseId);
            root.setPname(courseBase.getName());
            root.setParentid("0");
            root.setGrade("1"); //1级
            root.setStatus("0");//未发布
            teachPlanRepository.save(root);
            return root.getId();
        }
        return teachPlanList.get(0).getId();

    }

    @Transactional
    public ResponseResult updateCourseBase(String courseId, CourseBase courseBase) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket) {
        if (courseMarket == null) {
            courseMarket = new CourseMarket();
        }

        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if (optional.isPresent()) {
            CourseMarket courseMarket1 = optional.get();
            courseMarket.setId(courseMarket1.getId());
        } else {
            //一起的不存在，则新增
            courseMarket.setId(courseId);
        }
        courseMarketRepository.save(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //向课程管理数据库添加课程和图片的关联关系
    @Transactional
    public ResponseResult addPicture(String courseId, String pictureId) {
        Optional<CoursePic> optional = coursePictureRepository.findById(courseId);
        CoursePic coursePic = null;
        //每个课程一张图片
        coursePic = optional.orElseGet(CoursePic::new);
        coursePic.setCourseid(courseId);
        coursePic.setPic(pictureId);
        coursePictureRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic findCoursePictureList(String courseId) {
        Optional<CoursePic> optional = coursePictureRepository.findById(courseId);
        return optional.orElse(null);
    }

    public ResponseResult deleteCoursePicture(String courseId) {
        Optional<CoursePic> optional = coursePictureRepository.findById(courseId);
        if (optional.isPresent()) {
            coursePictureRepository.deleteById(courseId);
            return new ResponseResult(CommonCode.SUCCESS);
        } else {
            return new ResponseResult(CommonCode.FAIL);
        }
    }
}
