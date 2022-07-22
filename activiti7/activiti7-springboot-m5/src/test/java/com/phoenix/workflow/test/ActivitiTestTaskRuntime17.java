package com.phoenix.workflow.test;

import com.phoenix.workflow.config.SecurityUtil;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.ReleaseTaskPayloadBuilder;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.ReleaseTaskPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.apache.commons.lang3.StringUtils;
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

/**
 * activiti7 新特性: taskRuntime API
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/22 11:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTestTaskRuntime17 {

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTestTaskRuntime17.class);

    /**
     * 查询指定用户的代办任务
     */
    @Test
    public void getWaitTaskList() {
        securityUtil.logInAs("xue");
        Page<Task> page = taskRuntime.tasks(Pageable.of(0, 10));
        List<Task> taskList = page.getContent();
        for (Task task : taskList) {
            LOGGER.info("task detail: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        }
    }

    /**
     * 完成 自己的任务
     */
    @Test
    public void completeTask() {
        //当前登录用户只能完成自己的任务
        securityUtil.logInAs("xue");
        String taskId = "5af64fb3-0918-11ed-989a-fedb4667e4da";
        taskRuntime.complete(
                TaskPayloadBuilder.complete().withTaskId(taskId).build()
        );
    }

    /**
     * 完成任务： 任务组 （多个候选人 多个候选组）
     * 拾取任务
     */
    @Test
    public void claimTask() {
        securityUtil.logInAs("meng");
        String taskId = "c6dfb1db-096c-11ed-9317-00ff044bad5f";
        //1.拾取任务
        Task task = taskRuntime.claim(
                TaskPayloadBuilder.claim().withTaskId(taskId).build()
        );
    }

    /**
     * 归还任务到任务组
     */
    @Test
    public void releaseTask() {
        securityUtil.logInAs("meng");
        String taskId = "c6dfb1db-096c-11ed-9317-00ff044bad5f";
        Task task = taskRuntime.release(
                TaskPayloadBuilder.release().withTaskId(taskId).build()
        );
    }

    @Test
    public void completeTaskGroup() {
        securityUtil.logInAs("gu");

        //1.查询任务呢
        String taskId = "c6dfb1db-096c-11ed-9317-00ff044bad5f";
        Task task = taskRuntime.task(taskId);
        if (task == null) {
            //没有任务，或者不是当前登录者的任务
            return;
        }
        //2.判断是否有办理人，没有办理人则拾取任务
        if (StringUtils.isEmpty(task.getAssignee())) {
            //拾取任务
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(taskId).build());
        }

        //3.完成任务
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).build());

    }


}
