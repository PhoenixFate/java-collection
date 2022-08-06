package com.phoenix.workflow.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@ApiOperation("流程实例条件请求类")
@Data
public class ProcessInstanceRequest extends BaseRequest{

    @ApiModelProperty("流程名称")
    private String processName;

    @ApiModelProperty("任务发起人")
    private String proposer;

}
