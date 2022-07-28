package com.phoenix.workflow.controller;

import com.phoenix.workflow.request.ProcessDefinitionRequest;
import com.phoenix.workflow.service.impl.ProcessDefinitionService;
import com.phoenix.workflow.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.ZipInputStream;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/27 18:09
 */
@Api("流程定义管理控制器")
@Slf4j
@RestController
@RequestMapping("/process")
@AllArgsConstructor
public class ProcessDefinitionController {

    private final ProcessDefinitionService processDefinitionService;

    @ApiOperation("条件分页查询相同key的最新版本的流程定义列表数据")
    @PostMapping("/list")
    public Result getProcessDefinitionList(@RequestBody ProcessDefinitionRequest request) {
        return processDefinitionService.getProcessDefinitionList(request);
    }

    @ApiOperation("更新流程状态，激活（启动）或者挂起（暂停）")
    @PutMapping("/state/{processDefinitionId}")
    public Result updateProcessDefinitionState(@PathVariable String processDefinitionId) {
        return processDefinitionService.updateProcessDefinitionState(processDefinitionId);
    }

    @DeleteMapping("/{deploymentId}")
    public Result deleteDeployment(@PathVariable String deploymentId, @RequestParam String key) {
        return processDefinitionService.deleteProcessDefinitionByDeploymentId(deploymentId, key);
    }

    @Autowired
    private RepositoryService repositoryService;

    @ApiOperation("导出流程定义文件（xml和png）")
    @GetMapping("/export/{type}/{processDefinitionId}")
    public void exportFile(@PathVariable String type,
                           @PathVariable String processDefinitionId,
                           HttpServletResponse response) throws IOException {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        //获取xml资源名
        String resourceName = "文件不存在";
        if ("xml".equals(type)) {
            //获取xml文件名
            resourceName = processDefinition.getResourceName();
        } else if ("png".equals(type)) {
            //获取png资源名
            resourceName = processDefinition.getDiagramResourceName();
        }
        //查询相关的资源输入流 String deploymentId, String resourceName
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        //创建输出流
        response.setHeader("Content-Disposition","attachment; filename="+ URLEncoder.encode(resourceName,"UTF-8"));
        //流的拷贝需要放到设置请求头的下面，不然文件大于10K可能无法导出
        IOUtils.copy(inputStream,response.getOutputStream());
        response.flushBuffer();
    }

    @ApiOperation("上传zip、bpmn、xml后缀的文件来进行部署流程定义")
    @PostMapping("/file/deploy")
    public Result deployByFile(@RequestParam("file")MultipartFile file) throws IOException {
        //文件名+后缀名
        String filename = file.getOriginalFilename();
        // 文件后缀名
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

        DeploymentBuilder deployment = repositoryService.createDeployment();
        if("ZIP".equals(suffix)){
            //zip压缩包
            deployment.addZipInputStream(new ZipInputStream(file.getInputStream()));
        }else {
            //xml或者bpmn
            deployment.addInputStream(filename,file.getInputStream());
        }
        //部署名称
        deployment.name(filename.substring(0,filename.lastIndexOf(".")));
        //开始部署
        deployment.deploy();
        return Result.ok();
    }




}
