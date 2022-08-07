package com.phoenix.workflow.controller;

import com.phoenix.workflow.entity.Loan;
import com.phoenix.workflow.request.LoanRequest;
import com.phoenix.workflow.service.ILoanService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api("借款申请接口")
@Slf4j
@RestController
@RequestMapping("/loan")
@AllArgsConstructor
public class LoanController {

    private ILoanService loanService;

    @ApiOperation("新增借款申请")
    @PostMapping
    public Result add(@RequestBody Loan loan) {
        return loanService.add(loan);
    }

    @ApiOperation("条件分页查询借款申请列表数据")
    @RequestMapping("/list")
    public Result listPage(@RequestBody LoanRequest request) {
        return loanService.listPage(request);
    }

    @ApiOperation("查询借款详情信息")
    @GetMapping("/{id}")
    public Result detail(@PathVariable String id) {
        Loan loan = loanService.getById(id);
        return Result.ok(loan);
    }

    @ApiOperation("更新借款详信息")
    @PutMapping
    public Result update(@RequestBody Loan loan) {
        return loanService.update(loan);
    }

}
