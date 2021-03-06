package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 17:24
 **/
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<CmsPage> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

        //暂时用静态数据
        //定义queryResult
        // QueryResult<CmsPage> queryResult = new QueryResult<>();
        // List<CmsPage> list = new ArrayList<>();
        // CmsPage cmsPage = new CmsPage();
        // cmsPage.setPageName("测试页面");
        // list.add(cmsPage);
        // queryResult.setList(list);
        // queryResult.setTotal(1);
        //
        // QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        // return queryResponseResult;
        //调用service
        return cmsPageService.findList(page, size, queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.getById(id);
    }

    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id, cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsPageService.deleteById(id);
    }

    /**
     * 页面发布
     *
     * @param pageId 页面id
     * @return
     */
    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult postPage(@PathVariable("pageId") String pageId) throws IOException, TemplateException {
        //执行页面静态化
        return cmsPageService.postPage(pageId);
    }

    @Override
    @PostMapping("/save")
    public CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage) {
        return cmsPageService.savePage(cmsPage);
    }

    //课程一键发布
    @Override
    @PostMapping("/postPageQuickly")
    public CmsPostPageResult postPageQuickly(@RequestBody CmsPage cmsPage) throws IOException, TemplateException {
        return cmsPageService.postPageQuickly(cmsPage);
    }
}
