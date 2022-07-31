package com.phoenix.workflow.controller;

import com.phoenix.workflow.request.StartProcessInstanceRequest;
import com.phoenix.workflow.service.IProcessInstanceService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api
@Slf4j
@RestController
@RequestMapping("/instance")
@AllArgsConstructor
public class ProcessInstanceController {

    private IProcessInstanceService processInstanceService;

    @ApiOperation("提交申请，启动流程实例")
    @PostMapping("/start")
    public Result start(@RequestBody StartProcessInstanceRequest request){
        return processInstanceService.startProcess(request);
    }


    @ApiOperation("撤销申请")
    @DeleteMapping("/cancel/apply")
    public Result cancelApply(@RequestParam String businessKey,
                              @RequestParam String processInstanceId,
                              @RequestParam(defaultValue = "返回成功") String message){
        return processInstanceService.cancel(businessKey,processInstanceId,message);
    }

    @ApiOperation("通过流程实例id获取申请表组件名")
    @GetMapping("/form/name/{processInstanceId}")
    public Result getFormNameByProcessInstanceId(@PathVariable String processInstanceId){
        return processInstanceService.getFormNameByProcessInstanceId(processInstanceId);
    }

    @ApiOperation("通过流程实例id获取任务办理历史记录")
    @GetMapping("/history/list")
    public Result getHistoryInfoListByProcessInstanceId(@RequestParam("procInstId") String processInstanceId){
        return processInstanceService.getHistoryInfoListByProcessInstanceId(processInstanceId);
    }

    @ApiOperation("通过流程实例id获取历史流程图")
    @GetMapping("/history/image")
    public void getHistoryProcessImage(@RequestParam("procInstId") String processInstanceId, HttpServletResponse response) throws IOException {
        processInstanceService.getHistoryProcessImage(processInstanceId,response);
    }


}
