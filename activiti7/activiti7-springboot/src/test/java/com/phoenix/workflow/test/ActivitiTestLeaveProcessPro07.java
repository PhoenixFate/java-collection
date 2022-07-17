package com.phoenix.workflow.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.workflow.domain.User;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ActivitiTestLeaveProcessPro07 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiTest01.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 通过流程定义模型数据部署流程定义
     * ACT_RE_PROCDEF
     * ACT_RE_DEPLOYMENT
     * ACT_GE_BYTEARRAY
     * ACT_RE_MODEL 更新流程部署id，将模型与部署的流程定义绑定
     *
     * @throws Exception
     */
    @Test
    public void deployLeaveProcessPro() throws Exception {
        // 1. 查询流程定义模型json字节码
        String modelId = "6a3856bd-05e5-11ed-8295-864ef7cab3a9";
        byte[] jsonBytes = repositoryService.getModelEditorSource(modelId);
        if (jsonBytes == null) {
            System.out.println("模型数据为空，请先设计流程定义模型，再进行部署");
            return;
        }
        // 将json字节码转为 xml 字节码，因为bpmn2.0规范中关于流程模型的描述是xml格式的，而activiti遵守了这个规范
        byte[] xmlBytes = bpmnJsonToXml(jsonBytes);
        if (xmlBytes == null) {
            System.out.println("数据模型不符合要求，请至少设计一条主线流程");
            return;
        }
        // 2. 查询流程定义模型的图片
        byte[] pngBytes = repositoryService.getModelEditorSourceExtra(modelId);

        // 查询模型的基本信息
        Model model = repositoryService.getModel(modelId);

        // xml资源的名称 ，对应act_ge_bytearray表中的name_字段
        String processName = model.getName() + ".bpmn20.xml";
        // 图片资源名称，对应act_ge_bytearray表中的name_字段
        String pngName = model.getName() + "." + model.getKey() + ".png";

        // 3. 调用部署相关的api方法进行部署流程定义
        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName()) // 部署名称
                .addString(processName, new String(xmlBytes, "UTF-8")) // bpmn20.xml资源
                .addBytes(pngName, pngBytes) // png资源
                .deploy();

        // 更新 部署id 到流程定义模型数据表中
        model.setDeploymentId(deployment.getId());
        repositoryService.saveModel(model);

        System.out.println("部署成功");
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

    /**
     * 启动流程实例：分配流程定义中的流程变量值 （就可以获取到办理人）
     */
    @Test
    public void startProcessInstanceAssigneeUEL() {
        //流程变量
        Map<String, Object> variables = new HashMap<>();
        //流程变量值：基本数据类型，JavaBean，Map，List
        variables.put("assignee1", "meng");
        User user=new User();
        user.setUsername("xue");
        // Map<String,Object> userMap=new HashMap<>();
        // userMap.put("username","xue");
        variables.put("user",user);
        //设置在DeptService.findManagerByUserId(userId) userId这个参数值
        variables.put("userId","123");

        //启动流程实例（流程定义key，业务id，流程变量）
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveProcess", "9999", variables);

        LOGGER.info("启动流程实例成功！ {}",processInstance.getId());
    }

}
