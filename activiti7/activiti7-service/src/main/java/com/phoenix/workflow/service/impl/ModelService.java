package com.phoenix.workflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.phoenix.workflow.form.ModelAddForm;
import com.phoenix.workflow.request.ModelRequest;
import com.phoenix.workflow.service.IModelService;
import com.phoenix.workflow.utils.DateUtils;
import com.phoenix.workflow.utils.Result;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelService extends ActivitiService implements IModelService {

    @Override
    public Result add(ModelAddForm modelAddForm){
        int version=0;
        //1. 初始化空的模型
        Model model = repositoryService.newModel();
        model.setName(modelAddForm.getName());
        model.setKey(modelAddForm.getKey());
        model.setVersion(version);

        //metaInfo是一个json字符串，有固定格式
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(ModelDataJsonConstants.MODEL_NAME,modelAddForm.getName());
        objectNode.put(ModelDataJsonConstants.MODEL_REVISION,version);
        objectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,modelAddForm.getDescription());
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
        repositoryService.addModelEditorSource(model.getId(),editorNode.toString().getBytes(StandardCharsets.UTF_8));
        return Result.ok(model.getId());
    }

    @Override
    public Result getModelList(ModelRequest<Model> modelRequest) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if(StringUtils.isNotEmpty(modelRequest.getName())){
            modelQuery.modelNameLike("%" + modelRequest.getName() + "%");
        }
        if(StringUtils.isNotEmpty(modelRequest.getKey())){
            modelQuery.modelKey(modelRequest.getKey());
        }
        //根据创建时间降序排序
        modelQuery.orderByCreateTime().desc();
        //分页查询
        List<Model> modelList = modelQuery.listPage(modelRequest.getFirstResult(), modelRequest.getSize());
        //总记录数
        long total = modelQuery.count();

        List<Map<String,Object>> records=new ArrayList<>();
        for(Model model:modelList){
            Map<String,Object> data=new HashMap<>();
            data.put("id",model.getId());
            data.put("key",model.getKey());
            data.put("name",model.getName());
            data.put("version",model.getVersion());
            String description = JSONObject.parseObject(model.getMetaInfo())
                    .getString(ModelDataJsonConstants.MODEL_DESCRIPTION);
            data.put("description",description);
            data.put("createDate", DateUtils.format(model.getCreateTime()));
            data.put("updateDate", DateUtils.format(model.getLastUpdateTime()));
            records.add(data);
        }

        Map<String,Object> result=new HashMap<>();
        result.put("records",records);
        result.put("total",total);
        return Result.ok(result);
    }
}
