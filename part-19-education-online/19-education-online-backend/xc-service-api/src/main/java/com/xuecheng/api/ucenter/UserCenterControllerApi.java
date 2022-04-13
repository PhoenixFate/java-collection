package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"用户中心接口"})
public interface UserCenterControllerApi {

    @ApiOperation("根据用户账号查询用户信息")
    XcUserExt getUserExtensionByUsername(String username);

}
