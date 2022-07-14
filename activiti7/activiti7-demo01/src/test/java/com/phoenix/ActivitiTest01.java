package com.phoenix;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/14 10:58
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

    @Test
    public void getProcessEngine02() {
        // 方式二 等同于方式一
        ProcessEngineConfiguration processEngineConfigurationFromResourceDefault = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        //源码   return createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
        ProcessEngine processEngine = processEngineConfigurationFromResourceDefault.buildProcessEngine();
        LOGGER.info("default process engine: {}", processEngine);
    }

    @Test
    public void getProcessEngine03() {
        // 方式三 指定特定名字的核心配置文件
        ProcessEngineConfiguration processEngineConfigurationFromResourceDefault = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti2.cfg.xml");
        ProcessEngine processEngine = processEngineConfigurationFromResourceDefault.buildProcessEngine();
        LOGGER.info("default process engine: {}", processEngine);
    }

    @Test
    public void getProcessEngine04() {
        // 方式三 指定特定名字的核心配置文件，和特定的beanName
        ProcessEngineConfiguration processEngineConfigurationFromResourceDefault = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti3.cfg.xml","processEngineConfiguration22");
        ProcessEngine processEngine = processEngineConfigurationFromResourceDefault.buildProcessEngine();
        LOGGER.info("default process engine: {}", processEngine);
    }
}
