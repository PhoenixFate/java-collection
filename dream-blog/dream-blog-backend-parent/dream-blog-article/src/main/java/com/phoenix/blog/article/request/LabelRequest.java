package com.phoenix.blog.article.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.Label;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 标签查询条件
 *
 * @Author phoenix
 * @Date 2022/10/12 17:21
 * @Version 1.0.0
 */
@ApiModel(value = "LabelRequest", description = "标签查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LabelRequest extends BaseRequest<Label> {

    @ApiModelProperty("标签名称")
    private String name;

    @ApiModelProperty("分类id")
    private String categoryId;
}
