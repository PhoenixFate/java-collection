package com.phoenix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @Author phoenix
 * @Date 3/8/23 15:09
 * @Version 1.0
 */
public class ActivitiTest02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    @Test
    public void deployByFile() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deployment = repositoryService.createDeployment()
                .name("test01")
                .addClasspathResource("processes/leave.bpmn")
                .addClasspathResource("processes/leave.png")
                .deploy();
        //4.输出部署结果
        LOGGER.info("部署 ID {}", deployment.getId());
        LOGGER.info("部署名称 {}", deployment.getName());
    }

    /**
     * 通过zip压缩包部署
     */
    @Test
    public void deployByZip() {
        //1.获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.获取部署流程定义的相关service
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.部署流程定义
        //读取zip资源压缩包，转成输入流
        InputStream inputStream = ReflectUtil.getResourceAsStream("processes/leave.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream)
                .name("请假申请流程-压缩包").deploy();

        //4.输出部署结果
        LOGGER.info("部署 ID {}", deployment.getId());
        LOGGER.info("部署名称 {}", deployment.getName());

    }

    /**
     * 查询部署好的流程定义数据
     */
    @Test
    public void getProcessDefinitionList() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> leaveProcess = processDefinitionQuery.processDefinitionKey("leaveProcess").orderByProcessDefinitionVersion().desc().list();
        for (ProcessDefinition process : leaveProcess) {
            System.out.println(process.getId());
            System.out.println(process.getName());
            System.out.println(process.getAppVersion());
            System.out.println(process.getCategory());
            System.out.println(process.getDescription());
            System.out.println(process.getDeploymentId());
            System.out.println(process.getDiagramResourceName());
            System.out.println(process.getEngineVersion());
            System.out.println(process.getKey());
            System.out.println(process.getVersion());
            System.out.println(process.getResourceName());
        }
    }
}
