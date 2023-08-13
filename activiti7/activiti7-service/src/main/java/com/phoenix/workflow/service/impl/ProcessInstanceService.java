package com.phoenix.workflow.service.impl;

import com.google.common.base.CaseFormat;
import com.phoenix.workflow.activiti.image.CustomProcessDiagramGenerator;
import com.phoenix.workflow.entity.BusinessStatus;
import com.phoenix.workflow.entity.ProcessConfig;
import com.phoenix.workflow.enums.BusinessStatusEnum;
import com.phoenix.workflow.request.ProcessInstanceRequest;
import com.phoenix.workflow.request.StartProcessInstanceRequest;
import com.phoenix.workflow.service.IBusinessStatusService;
import com.phoenix.workflow.service.IProcessConfigService;
import com.phoenix.workflow.service.IProcessInstanceService;
import com.phoenix.workflow.utils.DateUtils;
import com.phoenix.workflow.utils.Result;
import com.phoenix.workflow.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProcessInstanceService extends ActivitiService implements IProcessInstanceService {

    private final IProcessConfigService processConfigService;

    private final IBusinessStatusService businessStatusService;

    @Override
    public Result startProcess(StartProcessInstanceRequest request) {
        //1.通过业务路由名获取流程定义的key和表单组件名（表单组件名用于查询历史审批记录）
        ProcessConfig processConfig = processConfigService.getByBusinessRoute(request.getBusinessRoute());
        //2.表单组件名设置到流程变量中，后面查询历史审批记录需要
        //前端已经传递类当前的申请信息 （entity: {业务申请数据}）
        Map<String, Object> variables = request.getVariables();
        variables.put("formName", processConfig.getFormName());
        List<String> assignees = request.getAssignees();
        //有无办理人
        if (CollectionUtils.isEmpty(assignees)) {
            //无办理人，则不启动流程实例，直接结束
            return Result.error("请指定审批人");
        }
        //3.启动流程实例（提交申请）
        Authentication.setAuthenticatedUserId(UserUtils.getUsername());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processConfig.getProcessKey(),
                request.getBusinessKey(), variables);
        //将流程定义名称 作为 流程实例名称
        runtimeService.setProcessInstanceName(processInstance.getProcessInstanceId(),processInstance.getProcessDefinitionName());
        //4.设置任务办理人
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task task : taskList) {
            if (assignees.size() == 1) {
                //如果只有一个办理人，则直接设置为办理人
                taskService.setAssignee(task.getId(), assignees.get(0));
            } else {
                //多个办理人，则把多个办理人设置为候选人
                for (String assignee : assignees) {
                    taskService.addCandidateUser(task.getId(), assignee);
                }
            }
        }
        //5.更新业务状态为：办理中
        businessStatusService.updateState(request.getBusinessKey(), BusinessStatusEnum.PROCESS, processInstance.getId());
        return Result.ok();
    }

    @Override
    public Result cancel(String businessKey, String processInstanceId, String message) {
        //1.删除当前流程实例
        runtimeService.deleteProcessInstance(processInstanceId, UserUtils.getUsername() + "主动撤回来当前申请： " + message);

        //2.删除历史记录
        //historyService.deleteHistoricProcessInstance(processInstanceId);
        //historyService.deleteHistoricTaskInstance(processInstanceId);

        //3.更新业务状态
        return businessStatusService.updateState(businessKey, BusinessStatusEnum.CANCEL, "");
    }

    @Override
    public Result getFormNameByProcessInstanceId(String processInstanceId) {
        //通过流程实例id获取流程实例相关数据：流程变量
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables()
                .processInstanceId(processInstanceId)
                .singleResult();
        Object formName = historicProcessInstance.getProcessVariables()
                .get("formName");
        return Result.ok(formName);
    }

    @Override
    public Result getHistoryInfoListByProcessInstanceId(String processInstanceId) {
        //查询每个任务节点的历史办理情况
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        List<Map<String, Object>> records = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", historicTaskInstance.getId());
            result.put("taskName", historicTaskInstance.getName());
            result.put("processInstanceId", historicTaskInstance.getProcessInstanceId());
            result.put("startTime", historicTaskInstance.getStartTime());
            result.put("endTime", historicTaskInstance.getEndTime());
            result.put("status", historicTaskInstance.getEndTime() == null ? "待处理" : "已处理");
            result.put("assignee", historicTaskInstance.getAssignee());
            String deleteReason = historicTaskInstance.getDeleteReason();
            //获取撤回原因（如果撤回原因没有，则获取评论）
            if (StringUtils.isBlank(deleteReason)) {
                List<Comment> taskComments = taskService.getTaskComments(historicTaskInstance.getId());
                deleteReason = taskComments.stream().map(Comment::getFullMessage).collect(Collectors.joining("。"));
            }
            result.put("message", deleteReason);
            records.add(result);
        }

        return Result.ok(records);
    }

    @Override
    public void getHistoryProcessImage(String processInstanceId, HttpServletResponse response) throws IOException {
        //1.查询流程实例历史数据
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        //2.查询流程中已执行的节点，按照执行先后排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        //3。单独提取的高亮节点id（绿色的，已经结束的）
        List<String> highLightedActivityIdList = historicActivityInstanceList.stream()
                .map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());

        //4.正在执行的节点（红色的，已经完成的）
        List<Execution> runningActivityInstanceList = runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .list();
        List<String> runningActivityIdList=new ArrayList<>();
        for (Execution execution : runningActivityInstanceList) {
            if(StringUtils.isNotEmpty(execution.getActivityId())){
                runningActivityIdList.add(execution.getActivityId());
            }
        }
        //5.获取流程定义Model对象
        BpmnModel bpmnModel = repositoryService
                .getBpmnModel(historicProcessInstance
                        .getProcessDefinitionId());
        //6.实例化流程图生成器
        CustomProcessDiagramGenerator customProcessDiagramGenerator=new CustomProcessDiagramGenerator();
        //获取高亮连线id
        List<String> highLightedFlows = customProcessDiagramGenerator
                .getHighLightedFlows(bpmnModel, historicActivityInstanceList);
        InputStream inputStream = customProcessDiagramGenerator
                .generateDiagramCustom(bpmnModel, highLightedActivityIdList,
                runningActivityIdList, highLightedFlows, "宋体", "微软雅黑", "黑体");

        //7.响应相关的图片
        response.setContentType("image/svg+xml");
        byte[] bytes = IOUtils.toByteArray(inputStream);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public Result getProcessInstanceRunning(ProcessInstanceRequest request) {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        if(StringUtils.isNotEmpty(request.getProcessName())){
            processInstanceQuery.processInstanceNameLikeIgnoreCase(request.getProcessName());
        }
        if(StringUtils.isNotEmpty(request.getProposer())){
            processInstanceQuery.startedBy(request.getProposer());
        }
        List<ProcessInstance> processInstanceList = processInstanceQuery.listPage(request.getFirstResult(), request.getSize());
        long total = processInstanceQuery.count();
        List<Map<String,Object>> records=new ArrayList<>();
        for (ProcessInstance processInstance : processInstanceList) {
            Map<String,Object> result=new HashMap<>();
            result.put("processInstanceId",processInstance.getId());
            result.put("processInstanceName",processInstance.getName());
            result.put("processKey",processInstance.getProcessDefinitionKey());
            result.put("version",processInstance.getProcessDefinitionVersion());
            result.put("proposer",processInstance.getStartUserId());
            result.put("processStatus",processInstance.isSuspended()?"已暂停":"已启动");
            result.put("businessKey",processInstance.getBusinessKey());
            result.put("startTime", DateUtils.format(processInstance.getStartTime()));
            //查询当前实例的当前任务
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
            StringBuilder currentTaskInfo= new StringBuilder();
            for (Task task : taskList) {
                currentTaskInfo.append("任务名[").append(task.getName()).append("], 办理人[").append(task.getAssignee()).append("]<br>");
            }
            result.put("currTaskInfo", currentTaskInfo.toString());
            records.add(result);
        }

        Collections.sort(records, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                String startTime1 = (String)m1.get("startTime");
                String startTime2 = (String)m2.get("startTime");
                return startTime2.compareTo(startTime1);
            }
        });
        Map<String,Object> result=new HashMap<>();
        result.put("total",total);
        result.put("records",records);

        return Result.ok(result);
    }

    @Override
    public Result getProcessInstanceFinish(ProcessInstanceRequest request) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .finished()
                .orderByProcessInstanceEndTime().desc();
        if(StringUtils.isNotEmpty(request.getProcessName())){
            historicProcessInstanceQuery.processInstanceNameLikeIgnoreCase(request.getProcessName());
        }
        if(StringUtils.isNotEmpty(request.getProposer())){
            historicProcessInstanceQuery.startedBy(request.getProposer());
        }

        List<HistoricProcessInstance> historicProcessInstanceList = historicProcessInstanceQuery.listPage(request.getFirstResult(), request.getSize());
        long total = historicProcessInstanceQuery.count();
        List<Map<String,Object>> records=new ArrayList<>();
        for(HistoricProcessInstance historicProcessInstance:historicProcessInstanceList){
            Map<String,Object> result=new HashMap<>();
            result.put("processInstanceId",historicProcessInstance.getId());
            result.put("processInstanceName",historicProcessInstance.getName());
            result.put("processKey",historicProcessInstance.getProcessDefinitionKey());
            result.put("version",historicProcessInstance.getProcessDefinitionVersion());
            result.put("proposer",historicProcessInstance.getStartUserId());
            result.put("businessKey",historicProcessInstance.getBusinessKey());
            result.put("startTime",DateUtils.format(historicProcessInstance.getStartTime()));
            result.put("endTime",DateUtils.format(historicProcessInstance.getEndTime()));
            //删除原因
            result.put("deleteReason",historicProcessInstance.getDeleteReason());
            //业务状态
            BusinessStatus businessStatus = businessStatusService.getById(historicProcessInstance.getBusinessKey());
            if(businessStatus!=null){
                result.put("status",BusinessStatusEnum.getEumByCode(businessStatus.getStatus()).getDesc());
            }
            records.add(result);
        }

        Map<String,Object> result=new HashMap<>();
        result.put("total",total);
        result.put("records",records);
        return Result.ok(result);
    }

    @Override
    public Result deleteProcessInstanceAndHistory(String procInstId) {
        //1.查询历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInstId)
                .singleResult();

        //2.删除历史流程实例
        historyService.deleteHistoricProcessInstance(procInstId);
        historyService.deleteHistoricTaskInstance(procInstId);

        //3.更新流程业务状态，注意：流程实例id传递一个空字符串以更新数据
        businessStatusService.updateState(historicProcessInstance.getBusinessKey(),BusinessStatusEnum.DELETE,"");
        return Result.ok();
    }

    public static void main(String[] args) {
        String camelCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "THIS_STRING_SHOULD_BE_IN_CAMEL_CASE");
        System.out.println(camelCase);
        String camelCase2 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, "ProcessInstanceService");
        System.out.println(camelCase2);
    }
}
