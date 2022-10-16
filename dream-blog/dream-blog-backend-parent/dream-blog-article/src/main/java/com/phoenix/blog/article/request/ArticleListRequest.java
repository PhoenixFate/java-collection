package com.phoenix.blog.article.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.Article;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author phoenix
 * @Date 10/15/22 13:01
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ArticleListRequest", description = "文章列表查询条件")
public class ArticleListRequest extends BaseRequest<Article> {

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "标签id")
    private String labelId;
}
