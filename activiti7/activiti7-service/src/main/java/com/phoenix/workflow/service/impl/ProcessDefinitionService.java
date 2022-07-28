package com.phoenix.workflow.service.impl;

import com.phoenix.workflow.entity.ProcessConfig;
import com.phoenix.workflow.request.ProcessDefinitionRequest;
import com.phoenix.workflow.service.IProcessConfigService;
import com.phoenix.workflow.service.IProcessDefinitionService;
import com.phoenix.workflow.utils.DateUtils;
import com.phoenix.workflow.utils.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/27 17:41
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProcessDefinitionService extends ActivitiService implements IProcessDefinitionService {

    private final IProcessConfigService processConfigService;

    @Override
    public Result getProcessDefinitionList(ProcessDefinitionRequest request) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotEmpty(request.getName())) {
            // 查询条件
            processDefinitionQuery.processDefinitionNameLike("%" + request.getName() + "%");
        }
        if (StringUtils.isNotEmpty(request.getKey())) {
            processDefinitionQuery.processDefinitionKeyLike("%" + request.getKey() + "%");
        }
        //如果多个相同的key，只查询最新版本的流程定义
        processDefinitionQuery.latestVersion();

        //按key降序
        processDefinitionQuery.orderByProcessDefinitionKey().desc();

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(request.getFirstResult(), request.getSize());
        for (ProcessDefinition processDefinition : processDefinitionList) {
            log.info("processDefinition: {}", ToStringBuilder.reflectionToString(processDefinition, ToStringStyle.JSON_STYLE));
        }

        //总记录数
        long total = processDefinitionQuery.count();
        log.info("满足条件的流程定义总记录数： " + total);

        List<Map<String, Object>> records = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", processDefinition.getId());
            data.put("name", processDefinition.getName());
            data.put("key", processDefinition.getKey());
            data.put("version", processDefinition.getVersion());
            data.put("state", processDefinition.isSuspended() ? "已暂停" : "已启动");
            data.put("xmlName", processDefinition.getResourceName());
            data.put("pngName", processDefinition.getDiagramResourceName());

            String deploymentId = processDefinition.getDeploymentId();
            data.put("deploymentId", deploymentId);
            //关于部署时间的获取：先获取当前流程定义的部署信息
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            data.put("deploymentTime", DateUtils.format(deployment.getDeploymentTime()));

            //查询流程定义和业务相关的配置信息
            ProcessConfig processConfig = processConfigService.getByProcessKey(processDefinition.getKey());
            if (processConfig != null) {
                data.put("businessRoute", processConfig.getBusinessRoute());
                data.put("formName", processConfig.getFormName());
            }
            records.add(data);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("records", records);

        return Result.ok(result);
    }

    @Override
    public Result updateProcessDefinitionState(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        //判断是否挂机 true则挂机，false激活
        //对应 act_re_procdef 表中的 SUSPENSION_STATE_ 字段，1是激活，2是挂起
        if (processDefinition.isSuspended()) {
            //已挂机，将状态更新为激活
            //参数说明 参数1 流程定义id 参数2 是否激活(true 是否及联对应流程实例，true表示所有的流程实例都可以审批) 参数3 什么时候激活，如果为null 则立即激活
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
        } else {
            // 如果状态是：激活，将状态更新为：挂起
            // 参数 (流程定义id，是否挂起（true表示及联对应流程实例，对应都流程实例都不可以进行审批），激活时间)
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
        }
        return Result.ok();
    }

    @Override
    public Result deleteProcessDefinitionByDeploymentId(String deploymentId, String processKey) {
        //1.删除部署的流程定义
        repositoryService.deleteDeployment(deploymentId);

        //2.查询当前key对应的流程定义数据是否还有
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey).list();
        //3.如果没此key的流程定义，则将流程配置数据删除
        if (CollectionUtils.isEmpty(processDefinitionList)) {
            processConfigService.deleteByProcessKey(processKey);
        }
        return Result.ok();
    }
}
