package com.phoenix.workflow.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.ReflectUtils;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/15 14:53
 */
@SpringBootTest
public class ActivitiTest01 {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    private final static Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    @Test
    public void getProcessEngine() {
        LOGGER.info("processEngine: {}", processEngine);
    }

    @Test
    public void deployByZip() {
        //创建一次部署
        InputStream inputStream = ReflectUtil.getResourceAsStream("./processes/leave.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment().name("请假申请流程-压缩包-springboot")
                .addZipInputStream(zipInputStream).deploy();

        //输出部署内容
        LOGGER.info("部署ID: {}", deployment.getId());
        LOGGER.info("部署名称: {}", deployment.getName());

    }


}
