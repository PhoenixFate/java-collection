package com.phoenix.blog.article.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.Article;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author phoenix
 * @Date 10/14/22 00:11
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArticleUserRequest", description = "获取指定用户文章的查询条件")
public class ArticleUserRequest extends BaseRequest<Article> {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否公开，0不公开，1公开")
    private Integer isPublic;

}
