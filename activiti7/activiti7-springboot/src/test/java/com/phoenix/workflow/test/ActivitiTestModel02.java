package com.phoenix.workflow.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTestModel02 {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActivitiTestModel02.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 查询所有流程定义模型
     */
    @Test
    public void modelList() {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        List<Model> modelList = modelQuery.orderByCreateTime().desc().list();
        for (Model model : modelList) {
            LOGGER.info("模型id: {}; 模型名称: {}; 模型描述: {}; 模型标识key: {}; 模型版本号: {}",
                    model.getId(), model.getName(), model.getMetaInfo(), model.getKey(), model.getVersion());
        }
    }

    /**
     * 删除流程定义模型
     * ACT_RE_MODEL
     * ACT_GE_BYTEARRAY
     */
    @Test
    public void deleteModel() {
        String modelId = "361f1586-0468-11ed-9e09-c67b5f042936";
        repositoryService.deleteModel(modelId);
        LOGGER.info("删除成功！");
    }


    /**
     * 导出流程定义模型的资源zip压缩包
     */
    @Test
    public void exportZip() throws IOException {
        //1.查询模型基本信息
        String modelId = "eca687ff-04bd-11ed-b142-8e347b641ee7";
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            //2.查询流程定义模型的json字节码
            byte[] bpmnJsonBytes = repositoryService.getModelEditorSource(modelId);
            //2.1将json字节码转换为xml字节码
            byte[] bpmnXmlBytes = bpmnJsonToXml(bpmnJsonBytes);
            if (bpmnXmlBytes == null) {
                LOGGER.info("模型数据为空，请先设计流程定义模型，再导出");
            } else {
                //压缩包的文件名
                String zipName = model.getName() + "." + model.getKey() + ".zip";
                File file = new File("./" + zipName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                //实例和zip输出流
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
                //将xml添加到压缩包中 (指定xml的文件民： 请假流程.bpmn20.xml)
                zipOutputStream.putNextEntry(new ZipEntry(model.getName() + ".bpmn20.xml"));
                zipOutputStream.write(bpmnXmlBytes);
                //3.查询流程定义模型的图片字节码
                byte[] modelEditorSourceExtra = repositoryService.getModelEditorSourceExtra(modelId);
                if (modelEditorSourceExtra != null) {
                    //图片文件名 （请假流程.leaveProcess.png)
                    zipOutputStream.putNextEntry(new ZipEntry(model.getName() + "." + model.getKey() + ".png"));
                    zipOutputStream.write(modelEditorSourceExtra);
                }
                //4.以压缩包的方式导出流程定义模型文件
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                LOGGER.info("导出模型zip成功！ß");
            }

        }

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
    public void exportXml() throws IOException {
        //1.查询模型基本信息
        String modelId = "eca687ff-04bd-11ed-b142-8e347b641ee7";
        //流程图的字节对象
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
        String fileName = null;
        ByteArrayInputStream byteArrayInputStream = null;
        if (modelEditorSource != null) {
            //1.将json字节码转换成bpmnModel对象
            JsonNode jsonNode = objectMapper.readTree(modelEditorSource);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
            if (bpmnModel.getProcesses().size() > 0) {
                //有绘制流程图
                //2.BpmnModel转为xml字节码
                byte[] xmlBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
                byteArrayInputStream = new ByteArrayInputStream(xmlBytes);
                fileName = StringUtils.isBlank(bpmnModel.getMainProcess().getName()) ?
                        bpmnModel.getMainProcess().getId() : bpmnModel.getMainProcess().getName();
            }
        }

        if (fileName == null) {
            fileName = "模型数据为空，请先设计流程，再导出";
            byteArrayInputStream = new ByteArrayInputStream(fileName.getBytes(StandardCharsets.UTF_8));
        }

        //文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(new File("./" + fileName + ".bpmn20.xml"));
        IOUtils.copy(byteArrayInputStream, fileOutputStream);

        fileOutputStream.close();
        byteArrayInputStream.close();
        LOGGER.info("导出流程.bpm20.xml成功！");
    }

    /**
     * 通过流程模型 进行 流程定义部署
     * 流程图保存的时候是json串，引擎认识的却是符合bpmn2.0规范的xml，
     * 所以在首次的部署的时候要将json串转换为BpmnModel，
     * 再将BpmnModel转换成xml保存进数据库，以后每次使用就直接将xml转换成BpmnModel，
     * 这套操作确实有点啰嗦，实际项目中如果不用activiti自带的设计器，可以考虑用插件，直接生成的是xml，
     * 或者自己开发设计器，在后端生成节点及其属性，引擎有现成的节点实体，如：开始节点StartEvent，在线SequenceFlow等。
     * 涉及表：
     * ACT_RE_PROCDEF 新增数据: 流程定义数据
     * ACT_RE_DEPLOYMENT 新增数据: 流程部署数据
     * ACT_GE_BYTEARRAY 新增数据：将当前流程图绑定到此流程定义部署数据上
     * ACT_RE_MODEL 更新部署id 将模型与部署的流程定义绑定
     */
    @Test
    public void deployProcessDefinitionByModel() throws IOException {
        //1.查询流程定义json字节码
        String modelId = "eca687ff-04bd-11ed-b142-8e347b641ee7";
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


}
