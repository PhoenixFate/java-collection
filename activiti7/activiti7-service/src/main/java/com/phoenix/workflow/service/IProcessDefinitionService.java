package com.phoenix.workflow.service;

import com.phoenix.workflow.request.ProcessDefinitionRequest;
import com.phoenix.workflow.utils.Result;

/**
 * 流程定义 service
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/27 17:35
 */
public interface IProcessDefinitionService {

    /**
     * 条件分页查询流程定义列表数据
     *
     * @param request
     * @return
     */
    Result getProcessDefinitionList(ProcessDefinitionRequest request);

    /**
     * 更新流程定义状态：激活或者挂起
     *
     * @return
     */
    Result updateProcessDefinitionState(String processDefinitionId);

    Result deleteProcessDefinitionByDeploymentId(String deploymentId, String processKey);
}
