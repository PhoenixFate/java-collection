package com.phoenix.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.workflow.entity.Loan;
import com.phoenix.workflow.request.LoanRequest;
import com.phoenix.workflow.utils.Result;

public interface ILoanService extends IService<Loan> {

    Result add(Loan leave);

    /**
     * 分页查询借款列表
     * @param loanRequest request
     * @return Result
     */
    Result listPage(LoanRequest loanRequest);

    Result update(Loan leave);
}
