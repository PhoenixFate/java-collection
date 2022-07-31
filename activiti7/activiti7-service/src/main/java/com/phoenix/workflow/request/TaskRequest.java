package com.phoenix.workflow.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询待办任务列表条件")
public class TaskRequest extends BaseRequest{

    @ApiModelProperty("任务名称")
    private String taskName;
}
