package com.phoenix.workflow.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.zip.ZipInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTestDeployment03 {

    @Autowired
    private RepositoryService repositoryService;


    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    /**
     * 部署流程定义:
     * ACT_RE_PROCDEF     生成流程定义信息
     * ID 值的组成 （leaveProcess:1:4） （流程定义的唯一标识key:版本号:全局自增的标识值）
     * 每次部署，针对相同的流程定义的key，对应的version会自增1
     * ACT_RE_DEPLOYMENT  生成流程部署表
     * ACT_GE_BYTEARRAY   流程资源表
     */
    @Test
    public void deployByFile() throws FileNotFoundException {
        File xmlFile=new File("./leave.bpmn20.xml");
        FileInputStream fileInputStream=new FileInputStream(xmlFile);
        String xmlFilename=xmlFile.getName();

        File pngFile=new File("./leave.png");
        FileInputStream pngInputStream=new FileInputStream(pngFile);
        String pngFilename=pngFile.getName();

        //3.调用相关api方法进行部署
        Deployment deployment = repositoryService.createDeployment()
                .name("请假申请流程333")
                .addInputStream(xmlFilename,fileInputStream)
                .addInputStream(pngFilename,pngInputStream).deploy();
        //4.输出部署结果
        LOGGER.info("部署 ID {}", deployment.getId());
        LOGGER.info("部署名称 {}", deployment.getName());
    }

    /**
     * 通过zip压缩包部署
     */
    @Test
    public void deployByZip() throws FileNotFoundException {
        File file=new File("./leave.zip");
        FileInputStream inputStream=new FileInputStream(file);

        //3.部署流程定义
        //读取zip资源压缩包，转成输入流
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream)
                .name("请假申请流程222-压缩包").deploy();
        //4.输出部署结果
        LOGGER.info("部署 ID {}", deployment.getId());
        LOGGER.info("部署名称 {}", deployment.getName());
    }

    /**
     * 根据部署id来删除部署的流程定义数据
     */
    @Test
    public void deleteByDeploymentId(){
        String deploymentId="2a820daa-04f8-11ed-aac1-a24e87663e0c";
        //默认不是及联删除操作，如果有正在执行的流程实例，则删除会抛出异常，并且不会删除历史表数据
        repositoryService.deleteDeployment(deploymentId);

        //如果为true则是及联操作，如果流程定义启动来对应的流程实例，也可以进行删除，并且会删除历史数据
        //repositoryService.deleteDeployment(deploymentId,true);
        LOGGER.info("删除流程定义数据成功！");

    }

}
