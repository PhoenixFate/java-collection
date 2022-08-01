package com.phoenix.workflow.controller;

import com.phoenix.workflow.request.TaskRequest;
import com.phoenix.workflow.utils.DateUtils;
import com.phoenix.workflow.utils.Result;
import com.phoenix.workflow.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private final RepositoryService repositoryService;

    @ApiOperation("查询当前用户的待办任务")
    @PostMapping("/list/wait")
    public Result findWaitTask(@RequestBody TaskRequest taskRequest) {
        String username = UserUtils.getUsername();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateOrAssigned(username) //当前用户为候选人或者办理人
                .orderByTaskCreateTime()
                .desc();
        if (StringUtils.isNotEmpty(taskRequest.getTaskName())) {
            taskQuery.taskNameLikeIgnoreCase("%" + taskRequest.getTaskName() + "%");
        }
        //分页查询任务
        List<Task> taskList = taskQuery.listPage(taskRequest.getFirstResult(), taskRequest.getSize());
        long total = taskQuery.count();
        List<Map<String, Object>> records = new ArrayList<>();
        for (Task task : taskList) {
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", task.getId());
            result.put("taskName", task.getName());
            result.put("processStatus", task.isSuspended() ? "已暂停" : "已启动");
            result.put("taskCreatTime", DateUtils.format(task.getCreateTime()));
            result.put("processInstanceId", task.getProcessInstanceId());
            result.put("executionId", task.getExecutionId());
            result.put("processDefinitionId", task.getProcessDefinitionId());
            //任务办理人：如果是候选人则没有该值，是办理人才有该值
            result.put("taskAssignee", task.getAssignee());

            //查询流程实例，通过流程实例来获取流程名
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();


            result.put("processName", processInstance.getProcessDefinitionName());
            result.put("version", processInstance.getProcessDefinitionVersion());
            result.put("proposer", processInstance.getStartUserId());
            result.put("businessKey", processInstance.getBusinessKey());
            records.add(result);

        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("records", records);
        return Result.ok(result);
    }


    @ApiOperation("获取目标节点（下一个节点）")
    @GetMapping("/next/node")
    public Result getNextNodeInfo(@RequestParam String taskId) {
        //1.获取当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //2.从当前任务信息中获取流程定义id
        String processDefinitionId = task.getProcessDefinitionId();
        //3.拿到流程定义id后获取此bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        //4.通过任务节点id，来获取当前节点信息
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());

        //封装下一个用户任务节点信息
        List<Map<String, Object>> nextNodes = new ArrayList<>();
        //获取当前节点的下个节点（可能有多个）
        getNextNodes(flowElement, nextNodes);
        return Result.ok(nextNodes);
    }


    private void getNextNodes(FlowElement flowElement, List<Map<String, Object>> nextNodes) {
        //获取当前节点的连线信息
        List<SequenceFlow> outgoingFlows = ((FlowNode) flowElement).getOutgoingFlows();
        //当前节点的所有下一节点出口
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            //下一节点的目标元素
            FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
            if (targetFlowElement instanceof UserTask) {
                //用户任务，获取响应给前端设置办理人或者候选人
                HashMap<String, Object> node = new HashMap<>();
                node.put("id", targetFlowElement.getId()); //节点id
                node.put("name", targetFlowElement.getName());//节点名称
                nextNodes.add(node);
            } else if (targetFlowElement instanceof EndEvent) {
                //结束节点
                break;
            } else if (targetFlowElement instanceof ParallelGateway || targetFlowElement instanceof ExclusiveGateway) {
                //并行网关或者排他网关
                getNextNodes(targetFlowElement, nextNodes);
            }
        }

    }


}
