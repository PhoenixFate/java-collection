package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@FeignClient(value = "XC-SERVICE-MANAGE-CMS") //指定远程调用的服务名
public interface CmsPageClient {

    //根据页面id查询页面信息，远程调用cms信息
    //标识远程调用的http请求方法
    @GetMapping("/cms/page/get/{id}")
    CmsPage findCmsPageById(@PathVariable("id") String id);

    //feign远程调用，返回的对象需要有无参构造
    //添加页面（save），用于课程预览
    @PostMapping("cms/page/save")
    CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);

    @PostMapping("cms/page/postPageQuickly")
    CmsPostPageResult postPageQuickly(@RequestBody CmsPage cmsPage);
}
