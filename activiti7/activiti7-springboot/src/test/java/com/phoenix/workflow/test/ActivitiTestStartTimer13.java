package com.phoenix.workflow.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/19 10:15
 */
@SpringBootTest
public class ActivitiTestStartTimer13 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 开始定时器 部署
     * @throws IOException
     */
    @Test
    public void deploy() throws IOException {
        //1.查询流程定义json字节码
        String modelId = "c18d2621-0842-11ed-9224-9a14701eeabd";
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
        if (modelEditorSource == null) {
            LOGGER.info("模型数据为空，请先设计流程定义模型，再进行部署");
            return;
        }

        //1.1将json字节码转为xml字节码，因为bpmn2.0规范中关于流程模型的描述是xml格式的
        byte[] xmlBytes = bpmnJsonToXml(modelEditorSource);
        if (xmlBytes == null) {
            LOGGER.info("数据模型不符合要求，请至少设计一条主线流程");
            return;
        }
        //2.查询流程定义模型的图片
        byte[] pngBytes = repositoryService.getModelEditorSourceExtra(modelId);

        //查询模型基本信息
        Model model = repositoryService.getModel(modelId);

        //xml资源的名称
        String processXmlName = model.getName() + "Special" + ".bpmn20.xml";
        // 图片资源名称，对应的arc_ge_bytearray 表中的name_字段
        String processPngName = model.getName() + "Special." + model.getKey() + ".png";

        //3.通过部署相关的api方法进行部署流程定义
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name(model.getName() + "Special") //部署名称
                .addString(processXmlName, new String(xmlBytes, StandardCharsets.UTF_8))  //bpmn20.xml资源
                .addBytes(processPngName, pngBytes);

        Deployment deployment = deploymentBuilder.deploy();


        //更新部署id 到流程定义模型 数据表中
        model.setDeploymentId(deployment.getId());
        repositoryService.saveModel(model);

        LOGGER.info("通过流程模型 进行 流程定义部署  成功！");

    }

    private byte[] bpmnJsonToXml(byte[] bpmnJsonBytes) throws IOException {
        if (bpmnJsonBytes == null) {
            return null;
        }
        //1.将json字节码转换成bpmnModel对象
        JsonNode jsonNode = objectMapper.readTree(bpmnJsonBytes);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        if (bpmnModel.getProcesses().size() == 0) {
            return null;
        }
        //2.BpmnModel转为xml字节码
        return new BpmnXMLConverter().convertToXML(bpmnModel);
    }


    @Test
    public void findWaitTask(){
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("meng")
                .processDefinitionKey("StartTimer").list();
        for (Task task : taskList) {
            LOGGER.info("任务id {};任务名称 {}; 办理人 {}",
                    task.getId(), task.getName(),task.getAssignee());
        }
    }

    @Test
    public void completeTask(){
        //因为duration=2，所以直接走了行政审批的路线
        //如果参数中duration变量没有，会抛出异常
        taskService.complete("f806e1ee-074d-11ed-9a1c-00ff044bad5f");

    }


}
