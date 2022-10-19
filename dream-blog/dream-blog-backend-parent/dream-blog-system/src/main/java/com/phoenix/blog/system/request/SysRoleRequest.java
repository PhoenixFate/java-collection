package com.phoenix.blog.system.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysRoleRequest", description = "角色查询条件")
public class SysRoleRequest extends BaseRequest<SysRole> {

    @ApiModelProperty(value = "角色名称")
    private String name;

}
