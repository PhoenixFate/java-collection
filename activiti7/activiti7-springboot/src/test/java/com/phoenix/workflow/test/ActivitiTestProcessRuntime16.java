package com.phoenix.workflow.test;

import com.phoenix.workflow.config.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
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
     *          An Authentication object was not found in the SecurityContext
     *    原因：在使用了7版本，就需要使用security进行登录，并且登录用户要有ACTIVITI_USER角色
     * activiti7  7.1.0.M6 天生有bug
     */
    @Test
    public void getProcessDefinitionList(){
        securityUtil.logInAs("meng");


        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10)
        );

        int total = processDefinitionPage.getTotalItems();
        LOGGER.info("部署的流程定义总记录数： {}",total);
        List<ProcessDefinition> processDefinitionList = processDefinitionPage.getContent();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            LOGGER.info("单个部署的流程定义: {}", ToStringBuilder.reflectionToString(processDefinition, ToStringStyle.JSON_STYLE));
        }


    }

    @Test
    public void test01(){
        Deployment deployment = repositoryService.createDeploymentQuery().singleResult();

        LOGGER.info(deployment.getId());
    }


}
