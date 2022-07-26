package com.phoenix.workflow.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("分页查询基础类")
public class BaseRequest<T> implements Serializable {

    @ApiModelProperty("页码")
    private int current=1;

    @ApiModelProperty("每页显示多少条")
    private int size=10;

    /**
     * 针对mybatis-plus分页对象
     * @return
     */
    public Page<T> getPage(){
        return new Page<T>();
    }

    /**
     * activiti分页参数
     * @return
     */
    public Integer getFirstResult(){
        return (current-1)*size;
    }

}
