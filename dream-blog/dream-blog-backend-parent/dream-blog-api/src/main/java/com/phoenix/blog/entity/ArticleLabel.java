package com.phoenix.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章标签中间表
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("blog_article_label")
@ApiModel(value="ArticleLabel", description="文章标签中间表")
public class ArticleLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "文章id")
    private String articleId;

    @ApiModelProperty(value = "标签id")
    private String labelId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;


}
