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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api("流程定义模型管理")
@Slf4j
@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private IModelService modelService;

    @ApiOperation("新增流程定义模型数据")
    @PostMapping
    public Result add(@RequestBody ModelAddForm modelAddForm) {
        return modelService.add(modelAddForm);
    }

    @ApiOperation("条件分页查询流程定义模型数据")
    @PostMapping("/list")
    public Result modelList(@RequestBody ModelRequest<Model> modelRequest) {
        return modelService.getModelList(modelRequest);
    }

    @ApiOperation("通过流程模型定义id部署流程定义")
    @PostMapping("/deploy/{modelId}")
    public Result deploy(@PathVariable("modelId") String modelId) throws IOException {
        return modelService.deploy(modelId);
    }

    @ApiOperation("导出流程模型定义压缩包文件")
    @GetMapping("/export/zip/{modelId}")
    public void exportModelWithZip(@PathVariable("modelId") String modelId, HttpServletResponse response) throws IOException {
        modelService.exportModelWithZip(modelId, response);
    }

    @ApiOperation("删除流程定义模型")
    @DeleteMapping("{modelId}")
    public Result deleteModel(@PathVariable("modelId") String modelId) {
        modelService.deleteModel(modelId);
        return Result.ok();
    }

}
