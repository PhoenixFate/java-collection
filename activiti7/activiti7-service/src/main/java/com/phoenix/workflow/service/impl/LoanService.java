package com.phoenix.workflow.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.workflow.entity.Loan;
import com.phoenix.workflow.mapper.LoanMapper;
import com.phoenix.workflow.request.LoanRequest;
import com.phoenix.workflow.service.IBusinessStatusService;
import com.phoenix.workflow.service.ILoanService;
import com.phoenix.workflow.utils.Result;
import com.phoenix.workflow.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoanService extends ServiceImpl<LoanMapper, Loan> implements ILoanService {

    @Autowired
    private IBusinessStatusService businessStatusService;

    @Override
    public Result add(Loan leave) {
        //1.新增请假信息
        //当前登录用户即为申请人
        leave.setNickName(UserUtils.getUsername());
        leave.setUserId(UserUtils.getUsername());
        int count = baseMapper.insert(leave);
        //2.新增请假业务状态：待提交
        if (count == 1) {
            businessStatusService.add(leave.getId());
        }
        return Result.ok();
    }

    @Override
    public Result listPage(LoanRequest request) {
        if (StringUtils.isEmpty(request.getUsername())) {
            request.setUsername(UserUtils.getUsername());
        }
        IPage<Loan> loanPage = baseMapper.getLoanAndStatusList(request.getPage(), request);
        return Result.ok(loanPage);
    }

    @Override
    public Result update(Loan loan) {
        if (loan == null || StringUtils.isEmpty(loan.getId())) {
            return Result.error("数据不合法");
        }
        loan.setUpdateDate(new Date());
        baseMapper.updateById(loan);
        return Result.ok();
    }
}
