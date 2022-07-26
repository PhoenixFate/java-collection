package com.phoenix.workflow.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询模型条件请求类")
public class ModelRequest<T> extends BaseRequest<T>{

    @ApiModelProperty("模型名称")
    private String name;

    @ApiModelProperty("模型标识key")
    private String key;


}
