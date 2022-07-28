package com.phoenix.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.workflow.entity.BusinessStatus;
import com.phoenix.workflow.enums.BusinessStatusEnum;
import com.phoenix.workflow.mapper.BusinessStatusMapper;
import com.phoenix.workflow.service.IBusinessStatusService;
import org.springframework.stereotype.Service;

@Service
public class BusinessStatusService extends ServiceImpl<BusinessStatusMapper, BusinessStatus> implements IBusinessStatusService {
    @Override
    public int add(String businessKey) {
        BusinessStatus businessStatus=new BusinessStatus();
        //状态为待提交
        businessStatus.setStatus(BusinessStatusEnum.WAIT.getCode());
        businessStatus.setBusinessKey(businessKey);
        return baseMapper.insert(businessStatus);
    }
}
