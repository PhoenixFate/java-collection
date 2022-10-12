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
 * 评论信息表
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("blog_comment")
@ApiModel(value="Comment", description="评论信息表")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "-1表示正常回复，其他值表示是评论的回复")
    private String parentId;

    @ApiModelProperty(value = "评论者用户id")
    private String userId;

    @ApiModelProperty(value = "评论者用户昵称")
    private String nickName;

    @ApiModelProperty(value = "评论者头像url")
    private String userImage;

    @ApiModelProperty(value = "文章id")
    private String articleId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;


}
