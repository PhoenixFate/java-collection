package com.phoenix.workflow.controller;

import com.phoenix.workflow.enums.BusinessStatusEnum;
import com.phoenix.workflow.request.ProcessInstanceRequest;
import com.phoenix.workflow.request.StartProcessInstanceRequest;
import com.phoenix.workflow.service.IBusinessStatusService;
import com.phoenix.workflow.service.IProcessInstanceService;
import com.phoenix.workflow.utils.Result;
import com.phoenix.workflow.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
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

    private RuntimeService runtimeService;

    private IBusinessStatusService businessStatusService;

    @ApiOperation("提交申请，启动流程实例")
    @PostMapping("/start")
    public Result start(@RequestBody StartProcessInstanceRequest request) {
        return processInstanceService.startProcess(request);
    }


    @ApiOperation("撤销申请")
    @DeleteMapping("/cancel/apply")
    public Result cancelApply(@RequestParam String businessKey,
                              @RequestParam String processInstanceId,
                              @RequestParam(defaultValue = "返回成功") String message) {
        return processInstanceService.cancel(businessKey, processInstanceId, message);
    }

    @ApiOperation("通过流程实例id获取申请表组件名")
    @GetMapping("/form/name/{processInstanceId}")
    public Result getFormNameByProcessInstanceId(@PathVariable String processInstanceId) {
        return processInstanceService.getFormNameByProcessInstanceId(processInstanceId);
    }

    @ApiOperation("通过流程实例id获取任务办理历史记录")
    @GetMapping("/history/list")
    public Result getHistoryInfoListByProcessInstanceId(@RequestParam("procInstId") String processInstanceId) {
        return processInstanceService.getHistoryInfoListByProcessInstanceId(processInstanceId);
    }

    @ApiOperation("通过流程实例id获取历史流程图")
    @GetMapping("/history/image")
    public void getHistoryProcessImage(@RequestParam("procInstId") String processInstanceId, HttpServletResponse response) throws IOException {
        processInstanceService.getHistoryProcessImage(processInstanceId, response);
    }


    @ApiOperation("查询正在运行中的流程实例")
    @PostMapping("/list/running")
    public Result getProcessInstanceRunning(@RequestBody ProcessInstanceRequest request) {
        return processInstanceService.getProcessInstanceRunning(request);
    }

    @ApiOperation("挂起或者激活流程实例")
    @PutMapping("/state/{procInstId}")
    public Result updateProcessInstanceState(@PathVariable("procInstId") String processInstanceId) {
        //1.查询指定流程实例的数据
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        //2.判断当前流程实例的状态
        if (processInstance.isSuspended()) {
            //如果是已挂起，则更新为激活状态
            runtimeService.activateProcessInstanceById(processInstanceId);
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
        }
        return Result.ok();
    }


    @ApiOperation("作废流程实例，不会删除历史记录")
    @DeleteMapping("/{procInstId}")
    public Result deleteProcessInstance(@PathVariable String procInstId) {
        //1.查询流程实例的信息
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        //2.删除流程实例
        runtimeService.deleteProcessInstance(procInstId, UserUtils.getUsername() + "作废来当前流程申请");
        //3.更新业务状态
        return businessStatusService.updateState(processInstance.getBusinessKey(), BusinessStatusEnum.INVALID);
    }

    @ApiOperation("查询已结束的流程实例")
    @PostMapping("/list/finish")
    public Result getProcessInstanceFinish(@RequestBody ProcessInstanceRequest request) {
        return processInstanceService.getProcessInstanceFinish(request);
    }

    @ApiOperation("删除已结束的流程实例和历史记录")
    @DeleteMapping("/history/{procInstId}")
    public Result deleteProcessInstanceAndHistory(@PathVariable String procInstId) {
        return processInstanceService.deleteProcessInstanceAndHistory(procInstId);
    }


}
