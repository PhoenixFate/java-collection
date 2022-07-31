package com.phoenix.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.workflow.entity.BusinessStatus;
import com.phoenix.workflow.enums.BusinessStatusEnum;
import com.phoenix.workflow.mapper.BusinessStatusMapper;
import com.phoenix.workflow.service.IBusinessStatusService;
import com.phoenix.workflow.utils.Result;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public Result updateState(String businessKey, BusinessStatusEnum statusEnum, String processInstanceId) {
        //1.查询当前数据
        BusinessStatus businessStatus = baseMapper.selectById(businessKey);
        //2.设置状态值
        businessStatus.setStatus(statusEnum.getCode());
        businessStatus.setUpdateDate(new Date());
        if(processInstanceId!=null){
            businessStatus.setProcessInstanceId(processInstanceId);
        }
        //3.更新操作
        baseMapper.updateById(businessStatus);
        return Result.ok();
    }

    @Override
    public Result updateState(String businessKey, BusinessStatusEnum statusEnum) {
        return updateState(businessKey,statusEnum,null);
    }
}
