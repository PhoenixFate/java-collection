package com.phoenix.workflow.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 统一管理activiti提供的服务接口
 */
public class ActivitiService {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected HistoryService historyService;

    /**
     * activiti7 新的api
     * 用户需要拥有 ACTIVITI_USER 这个角色
     */
    @Autowired
    protected ProcessRuntime processRuntime;

    /**
     * activiti7 新的api
     * 用户需要拥有 ACTIVITI_USER 这个角色
     */
    @Autowired
    protected TaskRuntime taskRuntime;

}
