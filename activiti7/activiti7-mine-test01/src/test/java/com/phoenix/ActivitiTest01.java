package com.phoenix;

import org.activiti.engine.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author phoenix
 * @Date 3/8/23 14:53
 * @Version 1.0
 */
public class ActivitiTest01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);


    @Test
    public void getProcessEngine01() {
        // 方式一：工具类ProcessEngines获取流程引擎实例
        // 核心配置文件中 id 必须为 processEngineConfiguration
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        LOGGER.info("default process engine: {}", defaultProcessEngine);

    }

    /**
     * 获取activiti 核心服务接口
     */
    @Test
    public void getServices(){
        //获取流程引擎实例
        // 会在首次调用时初始化并构建一个流程引擎，此后始终返回相同的流程引擎。
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //流程仓库 Service，主要用于管理流程仓库，比如流程定义的控制管理（部署、删除、挂起、激活....）
        RepositoryService repositoryService = processEngine.getRepositoryService();
        LOGGER.info("repositoryService: "+repositoryService);
        //运行时 Service，可以处理所有正在运行状态的流程实例和任务等
        RuntimeService runtimeService = processEngine.getRuntimeService();
        LOGGER.info("runtimeService: "+runtimeService);
        //任务 Service，用于管理和查询任务，例如：签收、办理等
        TaskService taskService = processEngine.getTaskService();
        LOGGER.info("taskService: "+taskService);
        //历史 Service，可以查询所有历史数据，例如：流程实例信息、参与者信息、完成时间...
        HistoryService historyService = processEngine.getHistoryService();
        LOGGER.info("historyService: "+historyService);
        /*
         * RepositoryService可以用来部署流程定义（使用xml形式定义好的），一旦
         * 部署到Activiti（解析后保存到DB），那么流程定义就不会再变了，除了修改
         * xml定义文件内容；而DynamicBpmnService就允许我们在程序运行过程中
         * 去修改流程定义，例如：修改流程定义中的分配角色、优先级、流程流转的
         * 条件...
         */
        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
        LOGGER.info("dynamicBpmnService: "+dynamicBpmnService);
        //引擎管理Service，和具体业务无关，主要用于对Activiti流程引擎的管理和维护
        ManagementService managementService = processEngine.getManagementService();
        LOGGER.info("managementService: "+managementService);
    }

}
