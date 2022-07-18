package com.phoenix.workflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义任务监听器
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/18 17:31
 */
//这里不需要写注解
public class CustomTaskListener implements TaskListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        LOGGER.info("任务id {}; 任务名称 {}; 触发事件名称 {}",
                delegateTask.getId(), delegateTask.getName(), delegateTask.getEventName());

        //判断是否是总裁审批 是否是create事件
        if ("总裁审批".equals(delegateTask.getName())
                && "create".equalsIgnoreCase(delegateTask.getEventName())) {
            //当任务创建后指定任务的办理人
            //办理人需要在springSecurity中配置
            delegateTask.setAssignee("小学");
        } else {
            LOGGER.info("非 总裁审批的create事件");
        }

    }
}
