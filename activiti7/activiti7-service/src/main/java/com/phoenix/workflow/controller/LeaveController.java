package com.phoenix.workflow.controller;

import com.phoenix.workflow.entity.Leave;
import com.phoenix.workflow.service.ILeaveService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("请假申请接口")
@Slf4j
@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private ILeaveService leaveService;

    @ApiOperation("新增请假申请")
    @PostMapping
    public Result add(@RequestBody Leave leave){
        return leaveService.add(leave);
    }



}
