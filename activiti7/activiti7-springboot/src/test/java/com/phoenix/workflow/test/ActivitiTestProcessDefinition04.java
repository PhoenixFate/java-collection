package com.phoenix.workflow.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;

@SpringBootTest
public class ActivitiTestProcessDefinition04 {

    @Autowired
    private RepositoryService repositoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);


    /**
     * 分页条件查询流程定义列表数据
     */
    @Test
    public void getProcessDefinition(){
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        String name="请假";
        if(StringUtils.isNotEmpty(name)){
            // 查询条件
            processDefinitionQuery.processDefinitionNameLike("%"+name+"%");
        }
        //如果多个相同的key，只查询最新版本的流程定义
        processDefinitionQuery.latestVersion();

        //按key降序
        processDefinitionQuery.orderByProcessDefinitionKey().desc();

        int current=1;
        int size=10;
        int firstResult=(current-1)*size;

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(firstResult, size);
        for (ProcessDefinition processDefinition : processDefinitionList) {
            LOGGER.info("processDefinition: {}", ToStringBuilder.reflectionToString(processDefinition, ToStringStyle.JSON_STYLE));
        }

        //总记录数
        long count=processDefinitionQuery.count();
        LOGGER.info("满足条件的流程定义总记录数： "+count);
    }

    /**
     * 挂机与激活流程定义
     *
     * 流程定义被挂起：此流程定义下的所有流程实例不允许继续往后流转了，就被停止了。
     * 流程定义被激活：此流程定义下的所有流程实例允许继续往后流转。
     * 为什么会被挂起？
     * 可能当前公司的请假流程发现了一些不合理的地方，然后就把此流程定义挂起。
     * 流程不合理解决办法：
     * 方式一：可以先挂起流程定义，然后更新流程定义，然后激活流程定义。
     * 方式二：挂起了就不激活了，重新创建一个新的请假流程定义。
     *
     * 对应 act_re_procdef 表中的 SUSPENSION_STATE_ 字段，1是激活，2是挂起
     */
    @Test
    public void updateProcessDefinitionStatus(){
        String processDefinitionId="leaveProcess:8:53412f31-04f6-11ed-b813-a24e87663e0c";
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        //判断是否挂机 true则挂机，false激活
        //对应 act_re_procdef 表中的 SUSPENSION_STATE_ 字段，1是激活，2是挂起
        if(processDefinition.isSuspended()){
            //已挂机，将状态更新为激活
            //参数说明 参数1 流程定义id 参数2 是否激活(true 是否及联对应流程实例，true表示所有的流程实例都可以审批) 参数3 什么时候激活，如果为null 则立即激活
            repositoryService.activateProcessDefinitionById(processDefinitionId,true,null);
        }else {
            // 如果状态是：激活，将状态更新为：挂起
            // 参数 (流程定义id，是否挂起（true表示及联对应流程实例，对应都流程实例都不可以进行审批），激活时间)
            repositoryService.suspendProcessDefinitionById(processDefinitionId,true,null);
        }

    }

    /**
     * 导出下载流程定义相关都文件(.bpmn20.xml流程描述或.png图片资源)
     */
    @Test
    public void exportProcessDefinition() throws IOException {
        String processDefinitionId="leaveProcess:4:5eec34a2-04ea-11ed-b571-c6292f41825c";
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);

        //获取xml资源名
        String xmlName=processDefinition.getResourceName();
        //String deploymentId, String resourceName
        InputStream xmlInputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), xmlName);

        //创建输出流
        File file=new File("./export/"+xmlName);
        FileOutputStream xmlOutputStream=new FileOutputStream(file);

        IOUtils.copy(xmlInputStream,xmlOutputStream);
        xmlInputStream.close();
        xmlOutputStream.close();
        LOGGER.info("流程定义资源文件 {} 导出成功！ ",xmlName);

        //获取png资源名
        String pngName=processDefinition.getDiagramResourceName();
        InputStream pngInputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), pngName);
        //创建输出流
        File pngFile=new File("./export/"+pngName);
        FileOutputStream pngOutputStream=new FileOutputStream(pngFile);

        IOUtils.copy(pngInputStream,pngOutputStream);
        pngInputStream.close();
        pngOutputStream.close();
        LOGGER.info("流程定义资源文件 {} 导出成功！ ",pngName);
    }





}
