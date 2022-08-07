package com.phoenix.workflow.request;

import com.phoenix.workflow.entity.Loan;
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
@ApiModel("查询借款列表条件")
public class LoanRequest extends BaseRequest<Loan> implements Serializable {

    @ApiModelProperty("借款标题")
    private String purpose;

    @ApiModelProperty("流程审批状态")
    private Integer status;

    @ApiModelProperty("所属用户名")
    private String username;
}
