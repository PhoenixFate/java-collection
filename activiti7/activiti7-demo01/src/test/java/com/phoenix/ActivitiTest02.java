package com.phoenix;

import org.activiti.engine.*;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiTest02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    /**
     * 部署流程定义:
     * ACT_RE_PROCDEF     生成流程定义信息
     * ID 值的组成 （leaveProcess:1:4） （流程定义的唯一标识key:版本号:全局自增的标识值）
     * 每次部署，针对相同的流程定义的key，对应的version会自增1
     * ACT_RE_DEPLOYMENT  生成流程部署表
     * ACT_GE_BYTEARRAY   流程资源表
     */
    @Test
    public void deployByFile() {
        //1.获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.获取部署流程定义的相关service
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.调用相关api方法进行部署
        Deployment deployment = repositoryService.createDeployment()
                .name("请假申请流程").addClasspathResource("processes/leave.bpmn")
                .addClasspathResource("processes/leave.png").deploy();
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
    public void getProcessDefinitionList(){
        //1.获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.获取部署流程定义的相关service
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.获取流程定义查询对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //List<ProcessDefinition> processDefinitionList = processDefinitionQuery.processDefinitionId("leaveProcess:1:4").listPage(0, 10);
        List<ProcessDefinition> processDefinitionList1 = processDefinitionQuery.processDefinitionKey("leaveProcess").orderByProcessDefinitionVersion().desc().list();

        processDefinitionList1.forEach((processDefinition)->
                LOGGER.info("processDefinition: {}", ToStringBuilder.reflectionToString(processDefinition, ToStringStyle.JSON_STYLE))
        );

    }

    /**
     * 启动流程实例（提交申请）
     * ACT_HI_TASKINST  流程实例的历史任务信息
     * ACT_HI_PROCINST  流程实例的历史数据
     * ACT_HI_ACTINST   流程实例执行的节点历史信息
     * ACT_HI_IDENTITYLINK 流程实例的参与者历史信息
     * ACT_RU_EXECUTION 流程实例运行中的执行信息
     * ACT_RU_TASK 流程实例运行中的（节点）任务信息
     * ACT_RU_IDENTITYLINK  流程实例运行中的参与者的信息
     */
    @Test
    public void startProcessInstance(){
        //1.获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.获取RuntimeService 服务实例
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.启动流程实例（流程定义key processDefinitionKey）
        // 通过流程定义key启动定流程实例，找定是最新版本定流程定义数据
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveProcess");


        LOGGER.info("流程定义id {}",processInstance.getProcessDefinitionId());
        LOGGER.info("流程实例id {}",processInstance.getId());

    }

    /**
     * 查询某个代办人的待办任务
     */
    @Test
    public void getTaskByAssignee(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();


        //3.查询待办任务
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("leaveProcess")
                .taskAssignee("meng")
                .list();

        taskList.forEach((task ->{
            LOGGER.info("流程实例ID {}",task.getProcessDefinitionId());
            LOGGER.info("任务id {}",task.getId());
            LOGGER.info("任务名称 {}",task.getName());
            LOGGER.info("任务办理人 {}",task.getAssignee());
        }));


    }


    /**
     * 办理人办理任务
     */
    @Test
    public void completeTask() {
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();

        //3.查询待办任务
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("leaveProcess")
                .taskAssignee("meng")
                .list();

        taskList.forEach((task -> {
            LOGGER.info("流程实例ID {}", task.getProcessDefinitionId());
            LOGGER.info("任务id {}", task.getId());
            LOGGER.info("任务名称 {}", task.getName());
            LOGGER.info("任务办理人 {}", task.getAssignee());

        }));

        if(!CollectionUtils.isEmpty(taskList)){
            Task task = taskList.get(0);
            //4.完成任务
            taskService.complete(task.getId());
            LOGGER.info("完成任务 ！！！");
        }else {
            LOGGER.info("未查询到待办任务");
        }

    }





}
