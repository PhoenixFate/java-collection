package com.phoenix.workflow.controller;

import com.phoenix.workflow.enums.BusinessStatusEnum;
import com.phoenix.workflow.request.TaskCompleteRequest;
import com.phoenix.workflow.request.TaskRequest;
import com.phoenix.workflow.service.IBusinessStatusService;
import com.phoenix.workflow.utils.DateUtils;
import com.phoenix.workflow.utils.Result;
import com.phoenix.workflow.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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

    private final TaskRuntime taskRuntime;

    private final HistoryService historyService;

    private final IBusinessStatusService businessStatusService;

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


    @ApiOperation("完成任务")
    @PostMapping("/complete")
    public Result completeTask(@RequestBody TaskCompleteRequest request) {
        String taskId = request.getTaskId();
        //1.查询任务信息
        org.activiti.api.task.model.Task task = taskRuntime.task(taskId);
        if (task == null) {
            return Result.error("任务不存在或者该任务不你的任务");
        }
        String processInstanceId = task.getProcessInstanceId();

        //2.指定任务审批意见
        taskService.addComment(taskId, processInstanceId, request.getMessage());

        //3.完成任务
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).build());

        //4.查询下一个任务
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();

        if (CollectionUtils.isEmpty(taskList)) {
            //task.getBusinessKey() //m5的bug，应该是有值的，但是没有值
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //更新业务状态为已完成
            return businessStatusService.updateState(historicProcessInstance.getBusinessKey(), BusinessStatusEnum.FINISH);
        } else {
            Map<String, String> assigneeMap = request.getAssigneeMap();
            if (assigneeMap == null) {
                //如果没有办理人，直接将流程实例删除（非法删除）
                return deleteProcessInstance(processInstanceId);
            }
            //有办理人
            for (Task task1 : taskList) {
                if (StringUtils.isNotEmpty(task1.getAssignee())) {
                    //如果当前任务有办理人，则直接忽略，不用指定办理人
                    continue;
                }
                //根据当前任务节点id获取办理人
                String[] assignees = request.getAssignees(task1.getTaskDefinitionKey());
                if (ArrayUtils.isEmpty(assignees)) {
                    //没有办理人
                    return deleteProcessInstance(processInstanceId);
                }
                if (assignees.length == 1) {
                    //设置办理人
                    taskService.setAssignee(task1.getId(), assignees[0]);
                } else {
                    //多个人设置候选人
                    for (String assginee : assignees) {
                        taskService.addCandidateUser(task1.getId(), assginee);
                    }
                }
            }
        }
        return Result.ok();
    }

    private Result deleteProcessInstance(String processInstanceId) {
        runtimeService.deleteProcessInstance(processInstanceId, "审批节点未分配审批人，流程直接中断取消");
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        businessStatusService.updateState(historicProcessInstance.getBusinessKey(), BusinessStatusEnum.CANCEL);
        return Result.ok("审批节点未分配审批人，流程直接中断取消");
    }


}
