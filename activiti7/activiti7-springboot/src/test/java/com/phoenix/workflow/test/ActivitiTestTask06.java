package com.phoenix.workflow.test;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTestTask06 {

    @Autowired
    private TaskService taskService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    /**
     * 查询指定办理人或者候选人的待办任务
     */
    @Test
    public void findWaitTask(){
        //办理人
        String assignee="meng";
        //查询某个人为办理人的待办任务
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime().desc()
                .list();
        for (Task task : taskList) {
            LOGGER.info("one 办理人 task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        }


        //查询某个人为办理人或者候选人的待办任务
        List<Task> taskList1 = taskService.createTaskQuery()
                .taskCandidateOrAssigned(assignee)
                .orderByTaskCreateTime().desc()
                .list();
        for (Task task : taskList1) {
            LOGGER.info("one 办理人 or 候选人 task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        }
    }


}
