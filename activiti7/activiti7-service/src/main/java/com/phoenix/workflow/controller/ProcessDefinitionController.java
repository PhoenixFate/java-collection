package com.phoenix.workflow.controller;

import com.phoenix.workflow.request.ProcessDefinitionRequest;
import com.phoenix.workflow.service.impl.ProcessDefinitionService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/27 18:09
 */
@Api("流程定义管理控制器")
@Slf4j
@RestController
@RequestMapping("/process")
@AllArgsConstructor
public class ProcessDefinitionController {

    private final ProcessDefinitionService processDefinitionService;

    @ApiOperation("条件分页查询相同key的最新版本的流程定义列表数据")
    @PostMapping("/list")
    public Result getProcessDefinitionList(@RequestBody ProcessDefinitionRequest request) {
        return processDefinitionService.getProcessDefinitionList(request);
    }

    @ApiOperation("更新流程状态，激活（启动）或者挂起（暂停）")
    @PutMapping("/state/{processDefinitionId}")
    public Result updateProcessDefinitionState(@PathVariable String processDefinitionId) {
        return processDefinitionService.updateProcessDefinitionState(processDefinitionId);
    }

    @DeleteMapping("/{deploymentId}")
    public Result deleteDeployment(@PathVariable String deploymentId, @RequestParam String key) {
        return processDefinitionService.deleteProcessDefinitionByDeploymentId(deploymentId, key);
    }


}
