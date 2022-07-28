package com.phoenix.workflow.controller;

import com.phoenix.workflow.entity.ProcessConfig;
import com.phoenix.workflow.service.IProcessConfigService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/28 16:08
 */
@Api("流程配置接口")
@RestController
@RequestMapping("/processConfig")
public class ProcessConfigController {

    @Autowired
    private IProcessConfigService processConfigService;

    @ApiOperation("根据流程定义key查询自定义流程配置文件")
    @GetMapping("{processKey}")
    public Result getProcessConfigByProcessKey(@PathVariable("processKey") String processKey) {
        ProcessConfig processConfig = processConfigService.getByProcessKey(processKey);
        return Result.ok(processConfig);
    }

    @ApiOperation("新增活更新流程配置")
    @PutMapping()
    public Result saveOrUpdate(@RequestBody ProcessConfig processConfig) {
        boolean b = processConfigService.saveOrUpdate(processConfig);
        return Result.ok();
    }


}
