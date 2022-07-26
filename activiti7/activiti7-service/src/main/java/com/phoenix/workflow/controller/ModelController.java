package com.phoenix.workflow.controller;

import com.phoenix.workflow.form.ModelAddForm;
import com.phoenix.workflow.request.ModelRequest;
import com.phoenix.workflow.service.IModelService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("流程定义模型管理")
@Slf4j
@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private IModelService modelService;

    @ApiOperation("新增流程定义模型数据")
    @PostMapping
    public Result add(@RequestBody ModelAddForm modelAddForm){
        return modelService.add(modelAddForm);
    }

    @ApiOperation("条件分页查询流程定义模型数据")
    @PostMapping("/list")
    public Result modelList(@RequestBody ModelRequest<Model> modelRequest){
        return modelService.getModelList(modelRequest);
    }


}
