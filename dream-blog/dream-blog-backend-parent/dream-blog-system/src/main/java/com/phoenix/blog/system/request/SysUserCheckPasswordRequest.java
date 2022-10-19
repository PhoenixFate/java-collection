package com.phoenix.blog.system.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("SysUserCheckPasswordRequest")
public class SysUserCheckPasswordRequest implements Serializable {

    @ApiModelProperty(value = "用户ID", required = true)
    private String userId;

    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;

}
