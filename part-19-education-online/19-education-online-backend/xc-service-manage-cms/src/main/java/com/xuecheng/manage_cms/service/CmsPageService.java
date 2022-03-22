package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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

}
