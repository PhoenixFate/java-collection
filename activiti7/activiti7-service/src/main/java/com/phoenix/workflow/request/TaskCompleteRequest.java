package com.phoenix.workflow.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/1 17:58
 */
@Data
@ApiModel("完成任务请求类")
public class TaskCompleteRequest extends BaseRequest implements Serializable {

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("审批意见")
    private String message;

    @ApiModelProperty("下一个节点审批，key:节点id,value: 审批人集合, 多个人使用英文，分割")
    private Map<String, String> assigneeMap;

    public String getMessage() {
        return StringUtils.isEmpty(message) ? "同意" : message;
    }

    /**
     * 通过节点id获取审批人集合
     *
     * @param key
     * @return
     */
    public String[] getAssignees(String key) {
        if (assigneeMap == null) {
            return null;
        }
        return assigneeMap.get(key).split(",");
    }


}
