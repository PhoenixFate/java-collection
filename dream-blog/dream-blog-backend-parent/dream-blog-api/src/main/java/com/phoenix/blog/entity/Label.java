package com.phoenix.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("blog_label")
@ApiModel(value = "Label", description = "标签表")
public class Label implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，分布式id
     * IdType.ASSIGN_ID: 分配ID (主键类型为number或string）, 默认实现类 com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator(雪花算法)
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private String categoryId;
    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
    private String name;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 分类名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

}
