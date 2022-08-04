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
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @ApiOperation("签收（拾取）任务")
    @PostMapping("/claim")
    public Result claimTask(@RequestParam String taskId){
        //只能签收属于自己的任务，自己不在候选组中签收任务会抛出异常
        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(taskId).build());
        return Result.ok();
    }

    /**
     * 转办任务给别人办理
     * @param taskId 任务id
     * @param assigneeUserKey 转办对象userId
     * @return 是否成功
     */
    @ApiOperation("转办任务给别人办理")
    @PostMapping("/turn")
    public Result turnTask(@RequestParam String taskId, @RequestParam String assigneeUserKey){
        org.activiti.api.task.model.Task task = taskRuntime.task(taskId);
        //转办
        taskService.setAssignee(taskId,assigneeUserKey);
        String message=String.format("%s 转办任务 [%s] 给 %s 办理",UserUtils.getUsername(),task.getName(),assigneeUserKey);
        //处理意见
        taskService.addComment(taskId,task.getProcessInstanceId(),message);
        return Result.ok();
    }
    
    @ApiOperation("获取历史任务节点，用于驳回功能")
    @GetMapping("/back/nodes")
    public Result getBackNodes(@RequestParam String taskId){
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(UserUtils.getUsername()).singleResult();
        if(task==null){
            return Result.error("没有次任务或者不是该任务办理人");
        }
        //查询历史任务节点(当前节点也会查询出来)
        // List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
        //         .processInstanceId(task.getProcessInstanceId())
        //         .list();
        // 不把当前节点查询出来, 没有办理完的节点不查询，每条数据都有一个唯一值，我们使用随机数
        String sql =  "select rand() AS ID_, t2.* from " +
                " ( select distinct t1.TASK_DEF_KEY_, t1.NAME_ from " +
                "  ( select ID_, RES.TASK_DEF_KEY_, RES.NAME_, RES.START_TIME_, RES.END_TIME_ " +
                "   from ACT_HI_TASKINST RES " +
                "   WHERE RES.PROC_INST_ID_ = #{processInstanceId} and TASK_DEF_KEY_ != #{taskDefKey}" +
                "   and RES.END_TIME_ is not null order by RES.START_TIME_ asc " +
                "  ) t1 " +
                " ) t2";

        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createNativeHistoricTaskInstanceQuery()
                .sql(sql)
                .parameter("processInstanceId", task.getProcessInstanceId())
                .parameter("taskDefKey", task.getTaskDefinitionKey()) // 不把当前节点查询出来
                .list();

        List<Map<String,Object>> records=new ArrayList<>();
        for (HistoricTaskInstance historicProcessInstance : historicTaskInstanceList) {
            Map<String,Object> data=new HashMap<>();
            data.put("activityId",historicProcessInstance.getTaskDefinitionKey());
            data.put("activityName",historicProcessInstance.getName());
            records.add(data);
        }
        return Result.ok(records);
    }

    @ApiOperation("驳回历史节点")
    @PostMapping("/back")
    public Result backSuccess(@RequestParam String taskId,@RequestParam String targetActivityId){

        //1.查询当前任务信息
        Task task = taskService.createTaskQuery().taskId(taskId)
                .taskAssignee(UserUtils.getUsername())
                .singleResult();
        if(task==null){
            return Result.error("当前任务不存在或者当前用户不是任务办理人");
        }
        String processInstanceId = task.getProcessInstanceId();

        //2.获取流程模型实例 BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //3.获取当前节点信息
        FlowNode currentFlowElement = (FlowNode)bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //4.获取当前节点的原出口连线
        List<SequenceFlow> outgoingFlows = currentFlowElement.getOutgoingFlows();

        //5.临时存储当前节点的原出口连线
        List<SequenceFlow> originalSequenceFlowList=new ArrayList<>();
        originalSequenceFlowList.addAll(outgoingFlows);

        //6.将当前节点的原出口清空
        outgoingFlows.clear();

        //7.获取目标节点信息
        FlowNode targetFlowNode = (FlowNode)bpmnModel.getFlowElement(targetActivityId);

        //8.获取驳回的新流向节点
        //获取目标节点的入口连线
        List<SequenceFlow> incomingFlows=targetFlowNode.getIncomingFlows();
        //存储所有目标出口
        List<SequenceFlow> allSequenceFlow=new ArrayList<>();
        for (SequenceFlow incomingFlow : incomingFlows) {
            //找到入口连线的源头（父节点）
            FlowNode sourceFlowElement = (FlowNode)incomingFlow.getSourceFlowElement();
            //获取目标节点的父组件的所有出口
            List<SequenceFlow> outgoingFlows1 = sourceFlowElement.getOutgoingFlows();
            allSequenceFlow.addAll(outgoingFlows1);
        }
        //9.将当前节点的出口设置为新节点
        currentFlowElement.setOutgoingFlows(allSequenceFlow);

        //10.完成当前任务，流程就会流向目标节点创建新目标任务
        //   删除已完成任务，删除已完成并执行任务的执行数据 act_ru_execution
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task1 : taskList) {
            if(taskId.equals(task1.getId())){
                //当前任务，完成当前任务
                String message = String.format("【%s 驳回任务 %s => %s】", UserUtils.getUsername(), task.getName(), targetFlowNode.getName());
                taskService.addComment(task1.getId(),processInstanceId,message);
                //完成任务，就会进行驳回到目标节点
                taskService.complete(taskId);
                //删除执行表中 is_active_=0到执行数据 使用command自定义模型
            }else {
                //删除其他未完成的并行任务
                //taskService.delegateTask(taskId); //注意这种方式删除不掉，会报错：流程正在运行中，无法删除
                //使用command自定义命令模型来删除，直接操作底层的删除表对应的方法
            }
        }

        //11.完成驳回功能后，将当前节点的原出口方向进行修复
        currentFlowElement.setOutgoingFlows(originalSequenceFlowList);

        //12.查询目标任务节点历史办理人
        List<Task> newTaskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task newTask : newTaskList) {
            //取之前到历史办理人
            HistoricTaskInstance oldTargetTask = historyService.createHistoricTaskInstanceQuery()
                    .taskDefinitionKey(newTask.getTaskDefinitionKey()) //节点id
                    .processInstanceId(processInstanceId)
                    .finished()  //已完成的才是历史
                    .orderByTaskCreateTime().desc() //最新办理的在最前面
                    .list().get(0);
            taskService.setAssignee(newTask.getId(),oldTargetTask.getAssignee());
        }


        return Result.ok();
    }





    
}
