package com.phoenix.blog.system.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysUserRequest", description = "用户查询条件")
public class SysUserRequest extends BaseRequest<SysUser> {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String mobile;

}
