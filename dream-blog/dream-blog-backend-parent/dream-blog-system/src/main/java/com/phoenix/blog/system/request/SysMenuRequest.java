package com.phoenix.blog.system.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "SysMenuRequest", description = "菜单列表查询条件")
public class SysMenuRequest {

    @ApiModelProperty(value = "菜单名称")
    private String name;

}
