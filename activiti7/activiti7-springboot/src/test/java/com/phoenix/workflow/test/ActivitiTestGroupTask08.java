package com.phoenix.workflow.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.workflow.domain.User;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTestGroupTask08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void startProcessInstanceAssigneeUEL() {
        //启动流程实例（流程定义key，业务id，流程变量）
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveProcessCandidate3", "6442");

        LOGGER.info("启动流程实例成功！ {}", processInstance.getId());
    }

    /**
     * 查询候选人任务
     */
    @Test
    public void getGroupTaskList(){
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("leaveProcessCandidate3")
                .taskCandidateUser("meng") //候选人
                .list();
        for (Task task : taskList) {
            LOGGER.info("任务id: {}; 任务名称: {}; 办理人: {}",task.getId(),task.getName(),task.getAssignee());
        }

    }

    /**
     * 候选人拾取任务
     */
    @Test
    public void claimTask(){
        String taskId="1d64f518-06bb-11ed-98ab-ea950b59aa14";
        String userId="meng";
        //候选人拾取任务, meng变为办理人
        //注意，即使拾取任务的办理人，不在候选人中，也可以拾取成功！
        //所以最后在拾取之前判断当前任务的候选人
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
        for (IdentityLink identityLink : identityLinkList) {
            LOGGER.info("候选人： {}",identityLink.getUserId());
        }
        taskService.claim(taskId,userId);
    }


    /**
     * 办理人归还候选任务
     */
    @Test
    public void assigneeToGroupTask(){
        String taskId="1d64f518-06bb-11ed-98ab-ea950b59aa14";
        String userId="meng";
        //候选人拾取任务, meng变为办理人
        //注意，即使拾取任务的办理人，不在候选人中，也可以拾取成功！
        //所以最后在拾取之前判断当前任务的候选人
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("leaveProcessCandidate3")
                .taskAssignee("meng") //候选人
                .singleResult();

        //2.归还组任务
        if(task!=null){
            //直接将办理人设置null，即归还到来任务中
            taskService.setAssignee(task.getId(),null);
        }

    }

    /**
     * 将办理人meng转办给其他人
     */
    @Test
    public void turnTask(){
        String taskId="1d64f518-06bb-11ed-98ab-ea950b59aa14";
        String assignee="meng";
        String candidateUser="xue"; //转办人
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("leaveProcessCandidate3")
                .taskAssignee(assignee) //候选人
                .taskId(taskId)
                .singleResult();

        //将任务转办给 xue 用户
        if(task!=null){
            taskService.setAssignee(task.getId(),candidateUser);
        }

    }

    @Test
    public void completeTask(){
        String taskId="1d64f518-06bb-11ed-98ab-ea950b59aa14";
        taskService.complete(taskId);
    }


}
