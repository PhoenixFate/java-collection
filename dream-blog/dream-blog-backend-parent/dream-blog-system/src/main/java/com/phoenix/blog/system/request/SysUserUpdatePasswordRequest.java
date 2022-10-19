package com.phoenix.blog.system.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("SysUserUpdatePasswordRequest")
public class SysUserUpdatePasswordRequest extends SysUserCheckPasswordRequest {

    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    private String repPassword;
}
