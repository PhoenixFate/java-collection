package com.phoenix.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.workflow.entity.ProcessConfig;
import com.phoenix.workflow.utils.Result;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/28 14:12
 */
public interface IProcessConfigService extends IService<ProcessConfig> {

    /**
     * 通过processKey 查询自定义的流程配置数据
     *
     * @param processKey
     * @return
     */
    ProcessConfig getByProcessKey(String processKey);

    /**
     * 通过processKey 删除自定义的流程配置数据
     *
     * @param processKey
     * @return
     */
    Result deleteByProcessKey(String processKey);

    /**
     * 通过业务路由名查询流程定义的配置信息（目前查询获取流程定义key）
     * @param businessRoute 业务路由
     * @return
     */
    ProcessConfig getByBusinessRoute(String businessRoute);

}
