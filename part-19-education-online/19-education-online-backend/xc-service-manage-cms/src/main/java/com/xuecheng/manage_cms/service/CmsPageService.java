package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    //用于打开下载流
    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 页面查询方法
     * @param page 页码，从1开始记数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        if(queryPageRequest==null){
            queryPageRequest=new QueryPageRequest();
        }


        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher=ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage=new CmsPage();
        //设置条件值
        if(StringUtils.isNoneEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNoneEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(StringUtils.isNoneEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义条件对象
        Example<CmsPage> example= Example.of(cmsPage,exampleMatcher);


        //分页参数
        if(page <=0){
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);//实现自定义条件查询并且分页查询
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    //新增页面
    public CmsPageResult add(CmsPage cmsPage){
        if(cmsPage==null){
            //抛出异常
        }

        //校验页面名称、站点id、页面webPath的唯一性
        //根据页面名称、站点id、页面webPath去cms_page集合，如果查询到，说明存在，如果查询不存在，则唯一
        CmsPage temp = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(temp!=null){
            //页面已经存在
            //抛出异常，异常内容就是页面已经存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //调用dao新增页面
        cmsPage.setPageId(null);//保证主键为空，使用mongo生成的主键
        CmsPage result = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,result);
    }

    public CmsPage getById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        return optional.orElse(null);
    }

    public CmsPageResult update(String id,CmsPage cmsPage){
        //根据id从数据库查询页面信息
        CmsPage one = this.getById(id);
        if(one!=null){
            //准备更新数据
            //设置要修改的数据
            //更新模板id
            if(StringUtils.isNotEmpty(cmsPage.getTemplateId())){
                one.setTemplateId(cmsPage.getTemplateId());
            }
            //更新所属站点
            if(StringUtils.isNotEmpty(cmsPage.getSiteId())){
                one.setSiteId(cmsPage.getSiteId());
            }
            //更新页面别名
            if(StringUtils.isNotEmpty(cmsPage.getPageAliase())){
                one.setPageAliase(cmsPage.getPageAliase());
            }
            //更新页面名称
            if(StringUtils.isNotEmpty(cmsPage.getPageName())){
                one.setPageName(cmsPage.getPageName());
            }
            //更新访问路径
            if(StringUtils.isNotEmpty(cmsPage.getPageWebPath())){
                one.setPageWebPath(cmsPage.getPageWebPath());
            }
            //更新物理路径
            if(StringUtils.isNotEmpty(cmsPage.getPagePhysicalPath())){
                one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            }
            if(StringUtils.isNotEmpty(cmsPage.getDataUrl())){
                one.setDataUrl(cmsPage.getDataUrl());
            }
            //提交修改
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS,one);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    public ResponseResult deleteById(String id){
        CmsPage temp = this.getById(id);
        if(temp!=null){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 页面静态化
     * 1.静态化程序获取页面的DataUrl
     * 2.静态化程序远程请求DataUrl获取数据模型
     * 3.静态化程序获取页面的模板信息
     * 4.执行页面静态化
     * @param pageId 页面id
     * @return 静态化string
     */
    public String getPageHtml(String pageId) throws IOException, TemplateException {
        //获取页面模型数据
        Map model=this.getModelByPageId(pageId);
        if(model==null){
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面的模板信息
        String templateContent = this.getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(templateContent)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行静态化
        String finalContent = this.generateHtml(templateContent, model);
        return finalContent;
    }

    //执行静态化
    private String generateHtml(String templateContent,Map model) throws IOException, TemplateException {
        //创建配置对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader=new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        Template template = configuration.getTemplate("template");
        //调用api进行静态化
        String finalContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        return finalContent;
    }


    private String getTemplateByPageId(String pageId) throws IOException {
        //取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage==null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面的模板id
        String templateId=cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            //模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFS中取出模板文件的内容
            //根据文件id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream=gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridResource对象
            GridFsResource gridFsResource=new GridFsResource(gridFSFile,gridFSDownloadStream);
            //从流中区数据
            String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            return content;
        }
        return null;
    }


    /**
     * //获取页面模型数据
     * @param pageId 页面id
     * @return
     */
    private Map getModelByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面的DataUrl
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            //页面的dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        return forEntity.getBody();
    }

    /**
     * 页面发布
     * @return
     */
    public ResponseResult postPage(String pageId) throws TemplateException, IOException {
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面静态化的文件存储到GridFs中
        CmsPage cmsPage = this.saveHtml(pageId, pageHtml);
        //向MQ发消息
        this.sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //保存html文件到GridFS中
    private CmsPage saveHtml(String pageId,String htmlContent) throws IOException {
        //先得到页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //将htmlContent内容转成输入流
        InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
        //将html文件内容保存到GridFS中
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        //将html文件id更新到cmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    //向mq发消息
    private void sendPostPage(String pageId){
        //得到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //创建消息对象
        Map<String,String> msg=new HashMap<>();
        msg.put("pageId",pageId);
        //转成json串
        String json = JSON.toJSONString(msg);
        //发送给mq
        //获得routingKey，就是站点id
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_ROUTING_CMS_POST_PAGE,siteId,json);

    }


}
