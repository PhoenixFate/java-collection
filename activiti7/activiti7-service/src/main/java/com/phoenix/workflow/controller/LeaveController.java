package com.phoenix.workflow.controller;

import com.phoenix.workflow.entity.Leave;
import com.phoenix.workflow.request.LeaveRequest;
import com.phoenix.workflow.service.ILeaveService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("请假申请接口")
@Slf4j
@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private ILeaveService leaveService;

    @ApiOperation("新增请假申请")
    @PostMapping
    public Result add(@RequestBody Leave leave) {
        return leaveService.add(leave);
    }


    @ApiOperation("条件分页查询请假申请列表数据")
    @RequestMapping("/list")
    public Result listPage(@RequestBody LeaveRequest leaveRequest) {
        return leaveService.listPage(leaveRequest);
    }

    @ApiOperation("查询请假详情信息")
    @GetMapping("/{id}")
    public Result detail(@PathVariable String id) {
        Leave leave = leaveService.getById(id);
        return Result.ok(leave);
    }

    @ApiOperation("更新请假详信息")
    @PutMapping
    public Result update(@RequestBody Leave leave) {
        return leaveService.update(leave);
    }

}
