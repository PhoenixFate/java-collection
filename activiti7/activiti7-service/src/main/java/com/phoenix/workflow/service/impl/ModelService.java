package com.phoenix.workflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.phoenix.workflow.form.ModelAddForm;
import com.phoenix.workflow.request.ModelRequest;
import com.phoenix.workflow.service.IModelService;
import com.phoenix.workflow.utils.DateUtils;
import com.phoenix.workflow.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ModelService extends ActivitiService implements IModelService {

    @Override
    public Result add(ModelAddForm modelAddForm) {
        int version = 0;

        //1. 初始化空的模型
        Model model = repositoryService.newModel();
        model.setName(modelAddForm.getName());
        model.setKey(modelAddForm.getKey());
        model.setVersion(version);

        //metaInfo是一个json字符串，有固定格式
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(ModelDataJsonConstants.MODEL_NAME, modelAddForm.getName());
        objectNode.put(ModelDataJsonConstants.MODEL_REVISION, version);
        objectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, modelAddForm.getDescription());
        model.setMetaInfo(objectNode.toString());
        //保存初始化的模型的基本信息数据
        repositoryService.saveModel(model);

        // 封装模型对象基础数据json串
        // {"id":"canvas","resourceId":"canvas","stencilset":{"namespace":"http://b3mn.org/stencilset/bpmn2.0#"},"properties":{"process_id":"未定义"}}
        ObjectNode editorNode = objectMapper.createObjectNode();
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.replace("stencilset", stencilSetNode);
        // 标识key
        ObjectNode propertiesNode = objectMapper.createObjectNode();
        propertiesNode.put("process_id", modelAddForm.getKey());
        editorNode.replace("properties", propertiesNode);
        repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
        return Result.ok(model.getId());
    }

    @Override
    public Result getModelList(ModelRequest<Model> modelRequest) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if (StringUtils.isNotEmpty(modelRequest.getName())) {
            modelQuery.modelNameLike("%" + modelRequest.getName() + "%");
        }
        if (StringUtils.isNotEmpty(modelRequest.getKey())) {
            modelQuery.modelKey(modelRequest.getKey());
        }
        //根据创建时间降序排序
        modelQuery.orderByCreateTime().desc();
        //分页查询
        List<Model> modelList = modelQuery.listPage(modelRequest.getFirstResult(), modelRequest.getSize());
        //总记录数
        long total = modelQuery.count();

        List<Map<String, Object>> records = new ArrayList<>();
        for (Model model : modelList) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", model.getId());
            data.put("key", model.getKey());
            data.put("name", model.getName());
            data.put("version", model.getVersion());
            String description = JSONObject.parseObject(model.getMetaInfo())
                    .getString(ModelDataJsonConstants.MODEL_DESCRIPTION);
            data.put("description", description);
            data.put("createDate", DateUtils.format(model.getCreateTime()));
            data.put("updateDate", DateUtils.format(model.getLastUpdateTime()));
            records.add(data);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        return Result.ok(result);
    }

    @Override
    public Result deploy(String modelId) throws IOException {
        //1.查询流程定义json字节码
        //String modelId = "eca687ff-04bd-11ed-b142-8e347b641ee7";
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
        if (modelEditorSource == null) {
            log.info("模型数据为空，请先设计流程定义模型，再进行部署");
            return Result.error("模型数据为空，请先设计流程定义模型，再进行部署");
        }

        //1.1将json字节码转为xml字节码，因为bpmn2.0规范中关于流程模型的描述是xml格式的
        byte[] xmlBytes = bpmnJsonToXml(modelEditorSource);
        if (xmlBytes == null) {
            log.info("数据模型不符合要求，请至少设计一条主线流程");
            return Result.error("数据模型不符合要求，请至少设计一条主线流程");
        }
        //2.查询流程定义模型的图片
        byte[] pngBytes = repositoryService.getModelEditorSourceExtra(modelId);

        //查询模型基本信息
        Model model = repositoryService.getModel(modelId);

        //xml资源的名称
        String processXmlName = model.getName() + ".bpmn20.xml";
        // 图片资源名称，对应的arc_ge_bytearray 表中的name_字段
        String processPngName = model.getName() + "." + model.getKey() + ".png";

        //3.通过部署相关的api方法进行部署流程定义
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name(model.getName()) //部署名称
                .addString(processXmlName, new String(xmlBytes, StandardCharsets.UTF_8))  //bpmn20.xml资源
                .addBytes(processPngName, pngBytes);
        Deployment deployment = deploymentBuilder.deploy();
        //更新部署id 到流程定义模型 数据表中
        model.setDeploymentId(deployment.getId());
        repositoryService.saveModel(model);
        log.info("通过流程模型 进行 流程定义部署 成功！");
        return Result.ok();
    }

    @Override
    public void exportModelWithZip(String modelId, HttpServletResponse response) throws IOException {
        //实例和zip输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        //1.查询模型基本信息
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            //2.查询流程定义模型的json字节码
            byte[] bpmnJsonBytes = repositoryService.getModelEditorSource(modelId);
            //2.1将json字节码转换为xml字节码
            byte[] bpmnXmlBytes = bpmnJsonToXml(bpmnJsonBytes);
            if (bpmnXmlBytes == null) {
                log.info("模型数据为空，请先设计流程定义模型，再导出");
            } else {
                //压缩包的文件名
                String zipName = model.getName() + "." + model.getKey();
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

                //设置contentType为流的形式
                response.setContentType("application/octet-stream");
                //设置header为附件文件形式，使得浏览器可以下载附件
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipName,"UTF-8")+".zip");
                //刷新响应流
                response.flushBuffer();
                log.info("导出模型zip成功！ß");
                zipOutputStream.closeEntry();
                zipOutputStream.close();
            }
        }
    }

    @Override
    public void deleteModel(String modelId) {
        repositoryService.deleteModel(modelId);
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

}
