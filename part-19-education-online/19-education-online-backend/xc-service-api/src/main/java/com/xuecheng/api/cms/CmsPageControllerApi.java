package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    //页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult<CmsPage> findList(int page, int size, QueryPageRequest queryPageRequest);
    //新增页面
    @ApiOperation("新增cms页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPage", value = "新增cms页面所需的参数", required = true,dataType="CmsPage")
    })
    CmsPageResult add(CmsPage cmsPage);
    //查询页面
    @ApiOperation("通过id查询页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "cms数据的id",required = true,paramType = "path",dataType = "String")
    })
    CmsPage findById(String id);

    @ApiOperation("修改页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "主键id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "cmsPage", value = "修改cms页面所需的参数", required = true,dataType="CmsPage")
    })
    CmsPageResult edit(String id,CmsPage cmsPage);
    //删除页面
    @ApiOperation("通过id删除页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "cms数据的id",required = true,paramType = "path",dataType = "String")
    })
    ResponseResult delete(String id);
    //页面发布
    @ApiOperation("页面发布")
    ResponseResult postPage(String pageId) throws Exception;

}
