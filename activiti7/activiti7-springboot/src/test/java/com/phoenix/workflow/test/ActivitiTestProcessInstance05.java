package com.phoenix.workflow.test;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ActivitiTestProcessInstance05 {

    @Autowired
    private RuntimeService runtimeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTestProcessInstance05.class);

    /**
     * 通过流程定义模型，进行部署流程定义，部署后会生成流程定义数据（相当于java类），此时生成的流程定义数据
     * 主要用于启动流程实例，一个流程定义 Java 类对应的可以创建无数个 java 流程实例对象。
     * 比如：
     * 部署好了请假流程定义，用户就可以发送请假流程了，即启动对应用户的请假流程实例，就可以开始对这个用户进
     * 行流程审批了。
     * 苍老师提交请假流程（启动一个新的流程实例）
     * 涛姐提交请假流程（启动一个新的流程实例）
     * 第N个人提交请求流程（启动一个新的流程实例)
     *
     * 启动流程实例，实际上就是提交流程申请。
     * 比如：
     * 提交请假申请流程，要先填写请假表单数据保存数据库，此请假表单ID对应的就是businessKey，在启动流程实例
     * 时将此 businessKey与此流程实例绑定，办理此流程实例任务，其实就是对此请假表单进行办理。
     * 开启流程实例指定的 businesskey，在流程实例的执行表 act_ru_execution 存储 businesskey
     *
     * 相关表
     * ACT_HI_TASKINST
     * ACT_HI_PROCINST 流程实例表
     * ACT_HI_ACTINST 节点实例
     * ACT_HI_IDENTITYLINK 流程实例相关办理人
     * ACT_RU_EXECUTION
     * ACT_RU_TASK
     * ACT_RU_IDENTITYLINK
     */
    @Test
    public void startProcessInstance(){
        //流程定义唯一标识key
        String processKey="leaveProcess";

        //业务id （子定义）
        String businessKey="10000";

        //启动当前流程实例的用户 会保存到ACT_HI_PROCINST表中start_user_id字段
        Authentication.setAuthenticatedUserId("王老师2");

        //启动流程实例，采用流程key对应都最新版本都流程定义数据
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey);

        //将流程定义名称 作为 流程实例名称
        runtimeService.setProcessInstanceName(processInstance.getId(),
                processInstance.getProcessDefinitionName()+"Instance2");

        LOGGER.info("启动流程实例成功："+processInstance.getProcessDefinitionId());
    }

    /**
     * 查询正在运行中的流程实例
     */
    @Test
    public void getRunningProcessInstance(){
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                .processInstanceNameLike("%请假%")
                .list();
        for (ProcessInstance processInstance : processInstanceList) {
            LOGGER.info("正在运行的流程实例： {}", ToStringBuilder.reflectionToString(processInstance, ToStringStyle.JSON_STYLE));
        }

    }

    /**
     * 激活或者挂起流程实例
     * 流程定义：
     *      激活： 激活流程定义后，对应所有的流程实例都可以继续向下流转
     *      挂起： 挂起流程定义后，对应所有的流程实例都不可以继续向下流转
     * 流程实例：
     *      激活： 激活流程实例后，此流程实例可以继续向下流转
     *      挂起： 挂起流程实例后，次流程实例不可以向下流转
     *
     */
    @Test
    public void updateProcessInstanceStatus(){
        String processInstanceId="b24bde63-05a6-11ed-a95a-46df58fc1a52";
        //1.查询当前流程实例的数据
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        //2.判断当前流程实例的状态
        if(processInstance.isSuspended()){
            //如果是已挂起，则更新为激活
            //已挂起，更新为激活状态
            runtimeService.activateProcessInstanceById(processInstanceId);
            LOGGER.info("激活成功！");

        }else  {
            //如果是已激活，则更新为挂起状态
            runtimeService.suspendProcessInstanceById(processInstanceId);
            LOGGER.info("挂起成功！");
        }
    }

    @Autowired
    private HistoryService historyService;


    /**
     * 删除流程实例
     *
     * 涉及到的数据表：
     *         ACT_RU_IDENTITYLINK
     *         ACT_RU_TASK
     *         ACT_RU_EXECUTION
     */
    @Test
    public void deleteProcessInstance(){
        String processInstanceId="b24bde63-05a6-11ed-a95a-46df58fc1a52";
        //流程实例是否存在
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(processInstance==null){
            LOGGER.info("流程实例 不存在！");
            return;
        }
        //删除流程实例（流程实例id，删除都原因），不会删除流程实例相关的历史数据
        runtimeService.deleteProcessInstance(processInstanceId,"xxxx作废来当前流程申请");

        //删除流程实例历史数据
        // historyService.deleteHistoricProcessInstance(processInstanceId);

    }


}
