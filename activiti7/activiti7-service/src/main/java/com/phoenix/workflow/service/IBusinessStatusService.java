package com.phoenix.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.workflow.entity.BusinessStatus;
import com.phoenix.workflow.enums.BusinessStatusEnum;
import com.phoenix.workflow.utils.Result;

public interface IBusinessStatusService extends IService<BusinessStatus> {

    /**
     * 新增数据： 状态 WAIT （1，待提交）
     * @param businessKey
     * @return
     */
    int add(String businessKey);


    /**
     * 更新流程的业务状态
     * @param businessKey 业务id
     * @param statusEnum 状态值
     * @param processInstanceId 流程实例id
     * @return
     */
    Result updateState(String businessKey, BusinessStatusEnum statusEnum,String processInstanceId);

    /**
     * 更新流程的业务状态
     * @param businessKey 业务id
     * @param statusEnum 状态值
     * @return
     */
    Result updateState(String businessKey, BusinessStatusEnum statusEnum);
}
