package com.phoenix.workflow.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;

import java.io.Serializable;
import java.util.List;

public class DeleteExecutionCommand implements Command<String>, Serializable {

    /**
     * 当前任务对应的act_ru_execution 执行id
     */
    private String executionId;

    public DeleteExecutionCommand(String executionId){
        this.executionId=executionId;
    }


    @Override
    public String execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        //获取执行数据
        ExecutionEntity executionEntity = executionEntityManager.findById(executionId);
        //通过当前执行数据的父执行，查询所有子执行的数据
        List<ExecutionEntity> allExecutionChildren = executionEntityManager.collectChildren(executionEntity.getParent());
        for (ExecutionEntity children : allExecutionChildren) {
            if(!children.isActive()){
                //将is_active_=0的执行数据删除，不然重复流向并执行任务后，重新审批并行任务
                //只要有一个节点完成（就是当前有2个并行任务，驳回前已经完成来1个任务），则并行网关就会汇聚往后走
                executionEntityManager.delete(children);
            }
        }
        return null;
    }
}
