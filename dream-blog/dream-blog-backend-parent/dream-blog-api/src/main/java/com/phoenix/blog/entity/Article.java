package com.phoenix.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章信息表
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("blog_article")
@ApiModel(value = "Article", description = "文章信息表")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "发布者用户id")
    private String userId;

    @ApiModelProperty(value = "发布者用户昵称")
    private String nickName;

    @ApiModelProperty(value = "发布者头像url")
    private String userImage;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章简介")
    private String summary;

    @ApiModelProperty(value = "文章主图地址")
    private String imageUrl;

    @ApiModelProperty(value = "md主体内容")
    private String mdContent;

    @ApiModelProperty(value = "html主体内容")
    private String htmlContent;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞数")
    private Integer likesNumber;

    @ApiModelProperty(value = "0: 已删除, 1:未审核，2:审核通过，3：审核未通过")
    private Integer status;

    @ApiModelProperty(value = "0：不公开，1：公开")
    private Integer isPublic;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    @ApiModelProperty(value = "文章标签集合")
    @TableField(exist = false)
    private List<Label> labelList;

    @ApiModelProperty(value = "文章标签id集合")
    @TableField(exist = false)
    private List<String> labelIdList;
}
