package com.phoenix.workflow.service;

import com.phoenix.workflow.form.ModelAddForm;
import com.phoenix.workflow.request.ModelRequest;
import com.phoenix.workflow.utils.Result;
import org.activiti.engine.repository.Model;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public interface IModelService {

    /**
     * 新增模型基本信息 （创建空的模型）
     *
     * @param modelAddForm
     * @return
     * @throws Exception
     */
    Result add(ModelAddForm modelAddForm);

    /**
     * 条件分页查询流程定义模型
     *
     * @param modelRequest
     * @return
     */
    Result getModelList(ModelRequest<Model> modelRequest);


    Result deploy(String modelId) throws IOException;

    /**
     * 导出流程定义模型的资源zip压缩包
     */
    void exportModelWithZip(String modelId, HttpServletResponse response) throws IOException;

    void deleteModel(String modelId);
}
