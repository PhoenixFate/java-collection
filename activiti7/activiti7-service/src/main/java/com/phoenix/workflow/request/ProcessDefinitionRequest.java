package com.phoenix.workflow.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/27 17:36
 */
@Data
@ApiModel("流程定义条件请求类")
public class ProcessDefinitionRequest extends BaseRequest{

    @ApiModelProperty("流程名称")
    private String name;

    @ApiModelProperty("标识key")
    private String key;


}
