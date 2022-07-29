package com.phoenix.workflow.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.workflow.entity.Leave;
import com.phoenix.workflow.mapper.LeaveMapper;
import com.phoenix.workflow.request.LeaveRequest;
import com.phoenix.workflow.service.IBusinessStatusService;
import com.phoenix.workflow.service.ILeaveService;
import com.phoenix.workflow.utils.Result;
import com.phoenix.workflow.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LeaveService extends ServiceImpl<LeaveMapper, Leave> implements ILeaveService {

    @Autowired
    private IBusinessStatusService businessStatusService;

    @Override
    public Result add(Leave leave) {
        //1.新增请假信息
        //当前登录用户即为申请人
        leave.setUsername(UserUtils.getUsername());
        int count = baseMapper.insert(leave);
        //2.新增请假业务状态：待提交
        if (count == 1) {
            businessStatusService.add(leave.getId());
        }
        return Result.ok();
    }

    @Override
    public Result listPage(LeaveRequest leaveRequest) {
        if (StringUtils.isEmpty(leaveRequest.getUsername())) {
            leaveRequest.setUsername(UserUtils.getUsername());
        }
        IPage<Leave> leavePage = baseMapper.getLeaveList(leaveRequest.getPage(), leaveRequest);
        return Result.ok(leavePage);
    }

    @Override
    public Result update(Leave leave) {
        if (leave == null || StringUtils.isEmpty(leave.getId())) {
            return Result.error("数据不合法");
        }
        leave.setUpdateDate(new Date());
        baseMapper.updateById(leave);
        return Result.ok();
    }
}
