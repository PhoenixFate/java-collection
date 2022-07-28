package com.phoenix.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.workflow.entity.Leave;
import com.phoenix.workflow.utils.Result;

public interface ILeaveService extends IService<Leave> {

    Result add(Leave leave);


}
