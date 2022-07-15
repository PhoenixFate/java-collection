package com.phoenix;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Deployment;
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

public class ActivitiTest03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    /**
     * 查询流程节点的历史节点信息
     */
    @Test
    public void historyInfo() {
        //1.获取流程引擎实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.历史相关的service
        HistoryService historyService = processEngine.getHistoryService();

        //3.获取查询对象
        //历史活动实例
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

        //4.开始查询
        List<HistoricActivityInstance> historicActivityInstanceList = historicActivityInstanceQuery
                .processInstanceId("12501")
                .orderByHistoricActivityInstanceStartTime().asc().list();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            LOGGER.info("流程定义id {}", historicActivityInstance.getProcessDefinitionId());
            LOGGER.info("流程实例id {}", historicActivityInstance.getProcessInstanceId());
            LOGGER.info("节点id {}", historicActivityInstance.getActivityId());
            LOGGER.info("节点名称 {}", historicActivityInstance.getActivityName());
            LOGGER.info("节点办理人 {}", historicActivityInstance.getAssignee());
            LOGGER.info("{}", historicActivityInstance.getStartTime());
            LOGGER.info("{}",historicActivityInstance.getEndTime());
        }

    }


}
