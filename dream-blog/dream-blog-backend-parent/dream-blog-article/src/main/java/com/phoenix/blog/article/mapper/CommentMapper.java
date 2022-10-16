package com.phoenix.blog.article.mapper;

import com.phoenix.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 通过文章id递归查询所有评论
     *
     * @param articleId 文章id
     * @return 所有评论
     */
    List<Comment> findByArticleId(@Param("articleId") String articleId);
}
