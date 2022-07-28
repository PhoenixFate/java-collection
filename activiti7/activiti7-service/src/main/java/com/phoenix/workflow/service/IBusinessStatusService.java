package com.phoenix.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.workflow.entity.BusinessStatus;

public interface IBusinessStatusService extends IService<BusinessStatus> {

    /**
     * 新增数据： 状态 WAIT （1，待提交）
     * @param businessKey
     * @return
     */
    int add(String businessKey);

}
