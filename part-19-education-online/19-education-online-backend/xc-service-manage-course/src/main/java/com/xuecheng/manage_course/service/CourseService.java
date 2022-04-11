package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachPlanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    @Autowired
    private CmsPageClient cmsPageClient;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CoursePubRepository coursePubRepository;


    @Value("${course-publish.dataUrlPreview}")
    private String DataUrlPreview;
    @Value("${course-publish.pagePhysicalPath}")
    private String PagePhysicalPath;
    @Value("${course-publish.pageWebPath}")
    private String PageWebPath;
    @Value("${course-publish.pagePhysicalPathPreview}")
    private String PagePhysicalPathPreview;
    @Value("${course-publish.pageWebPathPreview}")
    private String PageWebPathPreview;
    @Value("${course-publish.siteId}")
    private String SiteId;
    @Value("${course-publish.templateId}")
    private String TemplateId;
    @Value("${course-publish.previewUrl}")
    private String PreviewUrl;


    //课程计划查询
    public TeachPlanNode findTeachPlanPlan(String courseId) {
        return teachPlanMapper.selectListByCourseId(courseId);
    }

    public QueryResponseResult<CourseInfo> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        //page = page - 1; PageHelper不用减1
        if (size <= 0) {
            size = 10;
        }

        //分页，查询第一页，每页显示10条记录
        PageHelper.startPage(page, size);
        com.github.pagehelper.Page<CourseInfo> courseListWithPage = courseMapper.findCourseListWithPage();
        List<CourseInfo> result = courseListWithPage.getResult();
        long total = courseListWithPage.getTotal();

        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(result);//数据列表
        queryResult.setTotal(total);//数据总记录数
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, queryResult);
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());//设置起时间
        cal.add(Calendar.YEAR, 1);//增加一年
        courseMarket.setExpires(cal.getTime());

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

    //查询课程视图，包括课程基本信息、图片、营销、课程计划
    public CourseView getCourseView(String courseId) {
        CourseView courseView = new CourseView();
        //课程基本信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        courseBaseOptional.ifPresent(courseView::setCourseBase);
        //查询课程图片
        Optional<CoursePic> coursePicOptional = coursePictureRepository.findById(courseId);
        coursePicOptional.ifPresent(courseView::setCoursePic);
        //课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseId);
        courseMarketOptional.ifPresent(courseView::setCourseMarket);
        //课程计划信息
        TeachPlanNode teachPlanNode = teachPlanMapper.selectListByCourseId(courseId);
        courseView.setTeachPlanNode(teachPlanNode);
        return courseView;
    }

    @Transactional
    public CoursePublishResult coursePreview(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);

        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        CmsSite cmsSite = cmsSiteRepository.findBySiteName("课程预览");
        cmsPage.setSiteId(cmsSite.getSiteId());
        CmsTemplate cmsTemplate = cmsTemplateRepository.findByTemplateName("课程详情页面模板");
        cmsPage.setTemplateId(cmsTemplate.getTemplateId());
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase("课程预览页面-" + courseBase.getName());
        cmsPage.setPageWebPath(PageWebPathPreview);
        cmsPage.setPagePhysicalPath(PagePhysicalPathPreview);
        cmsPage.setDataUrl(DataUrlPreview + courseId);
        cmsPage.setPageType("1");
        cmsPage.setPageCreateTime(new Date());
        //请求cms添加页面
        //远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            //失败
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览的url
        String previewUrl = PreviewUrl + pageId;
        //返回CoursePublishResult对象（包括了页面预览的url）
        return new CoursePublishResult(CommonCode.SUCCESS, previewUrl);
    }

    /**
     * 发布课程
     * 发布课程的同时，把课程数据汇总到course_pub
     *
     * @param courseId
     * @return
     */
    @Transactional
    public CoursePublishResult publishCourse(String courseId) {
        //准备页面的信息
        CourseBase courseBase = this.getCourseBaseById(courseId);
        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        CmsSite cmsSite = cmsSiteRepository.findBySiteName("课程详情");
        cmsPage.setSiteId(cmsSite.getSiteId());
        CmsTemplate cmsTemplate = cmsTemplateRepository.findByTemplateName("课程详情页面模板");
        cmsPage.setTemplateId(cmsTemplate.getTemplateId());
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase("课程详情页面-" + courseBase.getName());
        cmsPage.setPageWebPath(PageWebPath);
        cmsPage.setPagePhysicalPath(PagePhysicalPath);
        cmsPage.setDataUrl(DataUrlPreview + courseId);
        cmsPage.setPageType("1");
        cmsPage.setPageCreateTime(new Date());

        //调用cms 一键发布 接口将课程详情页面发布到服务器
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuickly(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //保存课程的发布状态为已发布
        CourseBase courseBase1 = saveCourseStatus(courseId);
        if (courseBase1 == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //保存课程索引信息
        //创建coursePub对象并且保存到数据库
        CoursePub coursePub = this.createCoursePub(courseId);
        this.saveCoursePub(courseId,coursePub);
        //缓存课程索引信息
        //得到页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //更改课程状态
    private CourseBase saveCourseStatus(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);
        return courseBase;
    }

    private CoursePub saveCoursePub(String courseId,CoursePub coursePub){
        CoursePub coursePubNew=null;
        Optional<CoursePub> optional = coursePubRepository.findById(courseId);
        if(optional.isPresent()){
            coursePubNew=optional.get();
        }else {
            coursePubNew=new CoursePub();
        }
        BeanUtils.copyProperties(coursePub,coursePubNew);
        coursePubNew.setId(courseId);
        coursePubNew.setTimestamp(new Date());
        // SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // coursePubNew.setPubTime(simpleDateFormat.format(new Date()));
        coursePubNew.setPubTime(new Date());
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }


    //创建CoursePub
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            //将courseBase属性拷贝到coursePub中
            BeanUtils.copyProperties(courseBase, coursePub);
        }

        //课程营销
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        Optional<CoursePic> picOptional = coursePictureRepository.findById(id);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            coursePub.setPic(coursePic.getPic());
        }

        //课程计划
        TeachPlanNode teachPlanNode = teachPlanMapper.selectListByCourseId(id);
        String jsonString = JSON.toJSONString(teachPlanNode);
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

}
