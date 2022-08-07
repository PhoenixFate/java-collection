package com.phoenix.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.workflow.entity.Loan;
import com.phoenix.workflow.request.LoanRequest;
import org.apache.ibatis.annotations.Param;

public interface LoanMapper extends BaseMapper<Loan> {

    /**
     * 分页条件查询借款申请列表数据
     *
     * @param page 分页对象，mybatis-plus 规定分页对象一定要作为第一个参数
     * @return 借款列表
     */
    IPage<Loan> getLoanAndStatusList(IPage<Loan> page, @Param("request") LoanRequest loanRequest);

}
