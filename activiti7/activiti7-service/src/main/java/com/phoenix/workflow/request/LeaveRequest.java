package com.phoenix.workflow.request;

import com.phoenix.workflow.entity.Leave;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/29 9:53
 */
@Data
@ApiModel("查询请假列表条件")
public class LeaveRequest extends BaseRequest<Leave> implements Serializable {

    @ApiModelProperty("请假标题")
    private String title;

    @ApiModelProperty("流程审批状态")
    private Integer status;

    @ApiModelProperty("所属用户名")
    private String username;
}
