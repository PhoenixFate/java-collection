package com.phoenix.blog.article.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.Advert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author phoenix
 * @Date 10/16/22 21:01
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "AdvertRequest", description = "广告查询条件")
public class AdvertRequest extends BaseRequest<Advert> {

    @ApiModelProperty("广告标题")
    private String title;

    @ApiModelProperty("状态（0：禁用；1：正常）")
    private Integer status;
}
