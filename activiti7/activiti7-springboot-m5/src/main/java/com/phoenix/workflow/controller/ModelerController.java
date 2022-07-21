package com.phoenix.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * modeler 模型设计器 控制层
 */
@Controller
@RequestMapping("/model")
public class ModelerController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/create")
    public void create(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String name="请假流程模型";
        String key="leaveProcess";
        String description="请输入描述信息...";
        int version=1;
        //1. 初始化空的模型
        Model model = repositoryService.newModel();
        model.setName(name);
        model.setKey(key);
        model.setVersion(version);

        //metaInfo是一个json字符串，有固定格式

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(ModelDataJsonConstants.MODEL_NAME,name);
        objectNode.put(ModelDataJsonConstants.MODEL_REVISION,version);
        objectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,description);
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
        propertiesNode.put("process_id", key);
        editorNode.replace("properties", propertiesNode);


        repositoryService.addModelEditorSource(model.getId(),editorNode.toString().getBytes(StandardCharsets.UTF_8));

        response.sendRedirect(request.getContextPath()+"/modeler.html?modelId="+model.getId());
    }


}
