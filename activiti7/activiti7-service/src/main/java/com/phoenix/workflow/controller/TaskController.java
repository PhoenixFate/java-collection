package com.phoenix.workflow.controller;

import com.phoenix.workflow.request.TaskRequest;
import com.phoenix.workflow.utils.DateUtils;
import com.phoenix.workflow.utils.Result;
import com.phoenix.workflow.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
@Slf4j
@Api("任务管理控制接口")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final RuntimeService runtimeService;

    @ApiOperation("查询当前用户的待办任务")
    @PostMapping("/list/wait")
    public Result findWaitTask(@RequestBody TaskRequest taskRequest){
        String username = UserUtils.getUsername();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateOrAssigned(username) //当前用户为候选人或者办理人
                .orderByTaskCreateTime()
                .desc();
        if(StringUtils.isNotEmpty(taskRequest.getTaskName())){
            taskQuery.taskNameLikeIgnoreCase("%"+taskRequest.getTaskName()+"%");
        }
        //分页查询任务
        List<Task> taskList = taskQuery.listPage(taskRequest.getFirstResult(), taskRequest.getSize());
        long total = taskQuery.count();
        List<Map<String,Object>> records=new ArrayList<>();
        for (Task task : taskList) {
            Map<String,Object> result=new HashMap<>();
            result.put("taskId",task.getId());
            result.put("taskName",task.getName());
            result.put("processStatus",task.isSuspended()?"已暂停":"已启动");
            result.put("taskCreatTime", DateUtils.format(task.getCreateTime()));
            result.put("processInstanceId",task.getProcessInstanceId());
            result.put("executionId",task.getExecutionId());
            result.put("processDefinitionId",task.getProcessDefinitionId());

            //查询流程实例，通过流程实例来获取流程名
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();


            result.put("processName",processInstance.getProcessDefinitionName());
            result.put("version",processInstance.getProcessDefinitionVersion());
            result.put("proposer",processInstance.getStartUserId());
            result.put("businessKey",processInstance.getBusinessKey());
            records.add(result);

        }

        Map<String,Object> result=new HashMap<>();
        result.put("total",total);
        result.put("records",records);
        return Result.ok(result);
    }

}
