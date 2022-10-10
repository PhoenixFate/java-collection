package com.phoenix.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("blog_category") // Category实体类对应表mxg_category
@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，分布式id
     * IdType.ASSIGN_ID: 分配ID (主键类型为number或string）, 默认实现类 com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator(雪花算法)
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(1:正常，0:禁用)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}
