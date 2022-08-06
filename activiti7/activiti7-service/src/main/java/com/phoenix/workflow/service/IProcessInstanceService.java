package com.phoenix.workflow.service;

import com.phoenix.workflow.request.ProcessInstanceRequest;
import com.phoenix.workflow.request.StartProcessInstanceRequest;
import com.phoenix.workflow.utils.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IProcessInstanceService {

    /**
     * 提交申请启动流程实例
     * @param request 启动流程实例参数
     * @return 是否成功
     */
    Result startProcess(StartProcessInstanceRequest request);

    /**
     * 员工主动撤回提交的申请
     * @param businessKey
     * @param processInstanceId
     * @param message
     * @return
     */
    Result cancel(String businessKey,String processInstanceId,String message);

    /**
     * 通过流程实例id查询流程变量formName的值
     * @param processInstanceId
     * @return
     */
    Result getFormNameByProcessInstanceId(String processInstanceId);

    /**
     * 根据流程实例id查询历史审批信息
     * @param processInstanceId
     * @return
     */
    Result getHistoryInfoListByProcessInstanceId(String processInstanceId);

    /**
     * 查询流程实例审批历史流程图
     * @param processInstanceId
     * @param response
     */
    void getHistoryProcessImage(String processInstanceId, HttpServletResponse response) throws IOException;


    Result getProcessInstanceRunning(ProcessInstanceRequest request);

    Result getProcessInstanceFinish(ProcessInstanceRequest request);

    Result deleteProcessInstanceAndHistory(String procInstId);
}
