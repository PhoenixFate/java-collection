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


    //??????????????????
    public TeachPlanNode findTeachPlanPlan(String courseId) {
        return teachPlanMapper.selectListByCourseId(courseId);
    }

    public QueryResponseResult<CourseInfo> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        //????????????
        if (page <= 0) {
            page = 1;
        }
        //page = page - 1; PageHelper?????????1
        if (size <= 0) {
            size = 10;
        }

        //???????????????????????????????????????10?????????
        PageHelper.startPage(page, size);
        com.github.pagehelper.Page<CourseInfo> courseListWithPage = courseMapper.findCourseListWithPage(courseListRequest);
        List<CourseInfo> result = courseListWithPage.getResult();
        long total = courseListWithPage.getTotal();

        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(result);//????????????
        queryResult.setTotal(total);//??????????????????
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
        //????????????
        String courseId = teachPlan.getCourseid();
        //??????parentId????????????????????????id?????????parentId
        String parentId = teachPlan.getParentid();
        if (StringUtils.isEmpty(parentId)) {
            //???????????????????????????????????????
            parentId = this.getTeachPlanRoot(courseId);
        }
        //?????????
        TeachPlan teachPlanNew = new TeachPlan();
        //??????????????????teachPlan???????????????new?????????
        BeanUtils.copyProperties(teachPlan, teachPlanNew);
        teachPlanNew.setParentid(parentId);
        teachPlanNew.setCourseid(courseId);
        //?????????
        Optional<TeachPlan> optional = teachPlanRepository.findById(parentId);
        TeachPlan parentNode = optional.get();
        //?????????????????????????????????
        if (parentNode.getGrade().equals("1")) {
            teachPlanNew.setGrade("2");
        } else {
            teachPlanNew.setGrade("3");
        }
        teachPlanRepository.save(teachPlanNew);
        //????????????
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * ???????????????
     *
     * @param courseId ??????id
     * @return ?????????id
     */
    private String getTeachPlanRoot(String courseId) {
        //????????????id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        //??????????????????????????????
        List<TeachPlan> teachPlanList = teachPlanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachPlanList == null || teachPlanList.size() == 0) {
            //?????????????????????
            TeachPlan root = new TeachPlan();
            root.setCourseid(courseId);
            root.setPname(courseBase.getName());
            root.setParentid("0");
            root.setGrade("1"); //1???
            root.setStatus("0");//?????????
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
        cal.setTime(new Date());//???????????????
        cal.add(Calendar.YEAR, 1);//????????????
        courseMarket.setExpires(cal.getTime());

        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if (optional.isPresent()) {
            CourseMarket courseMarket1 = optional.get();
            courseMarket.setId(courseMarket1.getId());
        } else {
            //??????????????????????????????
            courseMarket.setId(courseId);
        }
        courseMarketRepository.save(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //????????????????????????????????????????????????????????????
    @Transactional
    public ResponseResult addPicture(String courseId, String pictureId) {
        Optional<CoursePic> optional = coursePictureRepository.findById(courseId);
        CoursePic coursePic = null;
        //????????????????????????
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

    //??????????????????????????????????????????????????????????????????????????????
    public CourseView getCourseView(String courseId) {
        CourseView courseView = new CourseView();
        //??????????????????
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        courseBaseOptional.ifPresent(courseView::setCourseBase);
        //??????????????????
        Optional<CoursePic> coursePicOptional = coursePictureRepository.findById(courseId);
        coursePicOptional.ifPresent(courseView::setCoursePic);
        //??????????????????
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseId);
        courseMarketOptional.ifPresent(courseView::setCourseMarket);
        //??????????????????
        TeachPlanNode teachPlanNode = teachPlanMapper.selectListByCourseId(courseId);
        courseView.setTeachPlanNode(teachPlanNode);
        return courseView;
    }

    @Transactional
    public CoursePublishResult coursePreview(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);

        //??????cmsPage??????
        CmsPage cmsPage = new CmsPage();
        CmsSite cmsSite = cmsSiteRepository.findBySiteName("????????????");
        cmsPage.setSiteId(cmsSite.getSiteId());
        CmsTemplate cmsTemplate = cmsTemplateRepository.findByTemplateName("????????????????????????");
        cmsPage.setTemplateId(cmsTemplate.getTemplateId());
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase("??????????????????-" + courseBase.getName());
        cmsPage.setPageWebPath(PageWebPathPreview);
        cmsPage.setPagePhysicalPath(PagePhysicalPathPreview);
        cmsPage.setDataUrl(DataUrlPreview + courseId);
        cmsPage.setPageType("1");
        cmsPage.setPageCreateTime(new Date());
        //??????cms????????????
        //????????????cms
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            //??????
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //?????????????????????url
        String previewUrl = PreviewUrl + pageId;
        //??????CoursePublishResult?????????????????????????????????url???
        return new CoursePublishResult(CommonCode.SUCCESS, previewUrl);
    }

    /**
     * ????????????
     * ????????????????????????????????????????????????course_pub
     *
     * @param courseId
     * @return
     */
    @Transactional
    public CoursePublishResult publishCourse(String courseId) {
        //?????????????????????
        CourseBase courseBase = this.getCourseBaseById(courseId);
        //??????cmsPage??????
        CmsPage cmsPage = new CmsPage();
        CmsSite cmsSite = cmsSiteRepository.findBySiteName("????????????");
        cmsPage.setSiteId(cmsSite.getSiteId());
        CmsTemplate cmsTemplate = cmsTemplateRepository.findByTemplateName("????????????????????????");
        cmsPage.setTemplateId(cmsTemplate.getTemplateId());
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase("??????????????????-" + courseBase.getName());
        cmsPage.setPageWebPath(PageWebPath);
        cmsPage.setPagePhysicalPath(PagePhysicalPath);
        cmsPage.setDataUrl(DataUrlPreview + courseId);
        cmsPage.setPageType("1");
        cmsPage.setPageCreateTime(new Date());

        //??????cms ???????????? ?????????????????????????????????????????????
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuickly(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //???????????????????????????????????????
        CourseBase courseBase1 = saveCourseStatus(courseId);
        if (courseBase1 == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //????????????????????????
        //??????coursePub??????????????????????????????
        CoursePub coursePub = this.createCoursePub(courseId);
        this.saveCoursePub(courseId, coursePub);
        //????????????????????????
        //????????????url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //??????????????????
    private CourseBase saveCourseStatus(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);
        return courseBase;
    }

    private CoursePub saveCoursePub(String courseId, CoursePub coursePub) {
        CoursePub coursePubNew = null;
        Optional<CoursePub> optional = coursePubRepository.findById(courseId);
        if (optional.isPresent()) {
            coursePubNew = optional.get();
        } else {
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub, coursePubNew);
        coursePubNew.setId(courseId);
        coursePubNew.setTimestamp(new Date());
        // SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // coursePubNew.setPubTime(simpleDateFormat.format(new Date()));
        coursePubNew.setPubTime(new Date());
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }


    //??????CoursePub
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            //???courseBase???????????????coursePub???
            BeanUtils.copyProperties(courseBase, coursePub);
        }

        //????????????
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        Optional<CoursePic> picOptional = coursePictureRepository.findById(id);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            coursePub.setPic(coursePic.getPic());
        }

        //????????????
        TeachPlanNode teachPlanNode = teachPlanMapper.selectListByCourseId(id);
        String jsonString = JSON.toJSONString(teachPlanNode);
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

}
