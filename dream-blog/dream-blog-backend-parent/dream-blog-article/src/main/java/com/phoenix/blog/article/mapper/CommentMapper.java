package com.phoenix.blog.article.mapper;

import com.phoenix.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 评论信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
