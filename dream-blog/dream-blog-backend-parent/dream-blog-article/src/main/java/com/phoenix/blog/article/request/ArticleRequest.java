package com.phoenix.blog.article.request;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.Article;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文章查询条件
 *
 * @Author phoenix
 * @Date 10/12/22 23:30
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "ArticleRequest", description = "文章查询条件")
public class ArticleRequest extends BaseRequest<Article> {

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "0: 已删除, 1:未审核，2:审核通过，3：审核未通过")
    private Integer status;

}
