package com.phoenix.blog.article.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author phoenix
 * @Date 2022/10/11 10:47
 * @Version 1.0.0
 */
@ApiModel(value = "CategoryRequest",description = "文章分类页列表查询对象")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true) //开启级联操作
public class CategoryRequest extends BaseRequest<Category> {

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;
    /**
     * 状态
     * 1：正常
     * 0：禁用
     */
    @ApiModelProperty("状态")
    private Integer status;
}
