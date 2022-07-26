package com.phoenix.workflow.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增模型表单类")
public class ModelAddForm {

    @ApiModelProperty("模型名称")
    private String name;

    @ApiModelProperty("模型标识key")
    private String key;

    @ApiModelProperty("模型描述")
    private String description;

}
