package com.phoenix.workflow.test;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/19 10:15
 */
@SpringBootTest
public class ActivitiTestHistory09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    @Autowired
    private HistoryService historyService;


    /**
     * 查询当前用户的已处理任务
     */
    @Test
    public void findCompleteTask() {
        List<HistoricTaskInstance> taskInstanceList = historyService.
                createHistoricTaskInstanceQuery()
                .taskAssignee("meng")
                .orderByTaskCreateTime()
                .desc()
                .finished()
                .includeProcessVariables() //获取流程变量
                .list();

        for (HistoricTaskInstance historicTaskInstance : taskInstanceList) {
            LOGGER.info("任务id {}; 任务名称 {}; 任务开始时间 {}; 任务结束时间 {}; 任务办理人 {}; " +
                            "流程定义id {}; 流程实例id {}; 流程业务key {}; 流程变量 {}",
                    historicTaskInstance.getId(),
                    historicTaskInstance.getName(),
                    historicTaskInstance.getStartTime(),
                    historicTaskInstance.getEndTime(),
                    historicTaskInstance.getAssignee(),
                    historicTaskInstance.getProcessDefinitionId(),
                    historicTaskInstance.getProcessInstanceId(),
                    historicTaskInstance.getBusinessKey(),
                    historicTaskInstance.getProcessVariables()
            );
        }
    }

    /**
     * 查询流程实例办理历史信息
     * select RES.* from ACT_HI_ACTINST RES WHERE RES.PROC_INST_ID_ = ? order by START_TIME_ asc
     */
    @Test
    public void historyInfo() {
        // 获取历史节点查询对象 ACT_HI_ACTINST 表
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

        String processInstanceId = "84e13eca-0683-11ed-ad89-00ff044bad5f";

        List<HistoricActivityInstance> historicActivityInstanceList = historicActivityInstanceQuery.processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            LOGGER.info("任务id {}; 任务开始时间 {}; 任务结束时间 {}; 任务办理人 {}; " +
                            "流程定义id {}; 流程实例id {};",
                    historicActivityInstance.getId(),
                    historicActivityInstance.getStartTime(),
                    historicActivityInstance.getEndTime(),
                    historicActivityInstance.getAssignee(),
                    historicActivityInstance.getProcessDefinitionId(),
                    historicActivityInstance.getProcessInstanceId()
            );
        }
    }

    /**
     * 查询已经结束的流程实例（已经结束的任务申请）
     */
    @Test
    public void getProcessInstanceFinished() {

        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime()
                .includeProcessVariables()
                .finished()
                .desc().list();

        for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
            LOGGER.info("流程实例 id {}; 流程实例名称 {}; 流程定义的key {}; 流程定义版本号 {}; " +
                            "业务id {}; 流程发起人 {}; 开始时间 {}; 结束时间 {}; 删除原因 {}",
                    historicProcessInstance.getId(),
                    historicProcessInstance.getName(),
                    historicProcessInstance.getProcessDefinitionKey(),
                    historicProcessInstance.getProcessDefinitionVersion(),
                    historicProcessInstance.getBusinessKey(),
                    historicProcessInstance.getStartUserId(),
                    historicProcessInstance.getStartTime(),
                    historicProcessInstance.getEndTime(),
                    historicProcessInstance.getDeleteReason()
            );
        }
    }

    /**
     * 删除已结束的流程实例
     * ACT_HI_DETAIL
     * ACT_HI_VARINST
     * ACT_HI_TASKINST
     * ACT_HI_PROCINST
     * ACT_HI_ACTINST
     * ACT_HI_IDENTITYLINK
     * ACT_HI_COMMENT
     */
    @Test
    public void deleteFinishedProcessInstance() {
        String processInstanceId = "a47f21b6-063d-11ed-9d7a-00ff044bad5f";

        //1.查询流程实例是否已结束
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .singleResult();

        //判断流程实例是否结束
        if (historicProcessInstance == null) {
            LOGGER.info("流程实例未结束 或者 不存在");
            return;
        }

        //2.删除已经结束的流程实例（如果流程实例未结束，会抛出异常）
        historyService.deleteHistoricProcessInstance(processInstanceId);

    }


}
