package com.phoenix.workflow.test;

import com.phoenix.workflow.config.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
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
 * activiti7 新特性: processRuntime API
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTestProcessRuntime16 {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTestProcessRuntime16.class);

    /**
     * 报错1. org.springframework.security.authentication.AuthenticationCredentialsNotFoundException:
     * An Authentication object was not found in the SecurityContext
     * 原因：在使用了7版本，就需要使用security进行登录，并且登录用户要有ACTIVITI_USER角色
     * activiti7  7.1.0.M6 天生有bug
     */
    @Test
    public void getProcessDefinitionList() {
        securityUtil.logInAs("meng");

        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));

        int total = processDefinitionPage.getTotalItems();
        LOGGER.info("部署的流程定义总记录数： {}", total);
        List<ProcessDefinition> processDefinitionList = processDefinitionPage.getContent();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            LOGGER.info("单个部署的流程定义: {}", ToStringBuilder.reflectionToString(processDefinition, ToStringStyle.JSON_STYLE));
        }
    }

    @Test
    public void startProcessByProcessRuntime() {
        securityUtil.logInAs("meng");
        String key = "leaveProcessM5";
        // 通过流程定义key启动的流程实例，采用的版本号是1版本，所以不推荐使用key启动，使用流程定义id启动
        ProcessInstance processInstance = processRuntime.start(
                ProcessPayloadBuilder.start()
                        .withProcessDefinitionId("leaveProcessM5:2:8933ad51-0912-11ed-a9a0-fedb4667e4da")
                        // .withProcessDefinitionKey(key)
                        .withName("流程实例名M5")
                        .withBusinessKey("45622")
                        .withVariable("user1", "xue")
                        .build()
        );
        LOGGER.info("流程实例启动成功！ {}", processInstance.getId());
    }

    @Test
    public void getProcessInstanceList() {
        securityUtil.logInAs("meng");
        Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0, 10),
                ProcessPayloadBuilder.processInstances().withProcessDefinitionKey("leaveProcessM5").build()
        );
        LOGGER.info("总条数：{}", processInstancePage.getTotalItems());
        List<ProcessInstance> processInstanceList = processInstancePage.getContent();
        for (ProcessInstance processInstance : processInstanceList) {
            LOGGER.info("process instance: {}", ToStringBuilder.reflectionToString(processInstance, ToStringStyle.JSON_STYLE));
        }
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void suspendProcessInstance() {
        securityUtil.logInAs("meng");
        String processId = "5af2f44c-0918-11ed-989a-fedb4667e4da";
        ProcessInstance processInstance = processRuntime.suspend(
                ProcessPayloadBuilder.suspend().withProcessInstanceId(processId).build()
        );
    }

    /**
     * 被挂起的流程实例重新激活
     */
    @Test
    public void resumeProcessInstance() {
        securityUtil.logInAs("meng");
        String processId = "5af2f44c-0918-11ed-989a-fedb4667e4da";
        ProcessInstance processInstance = processRuntime.resume(
                ProcessPayloadBuilder.resume(processId)
        );
    }

    /**
     * 删除流程实例
     */
    @Test
    public void deleteProcessInstance() {
        securityUtil.logInAs("meng");
        String processId = "5af2f44c-0918-11ed-989a-fedb4667e4da";
        processRuntime.delete(ProcessPayloadBuilder.delete(processId));
    }

}
