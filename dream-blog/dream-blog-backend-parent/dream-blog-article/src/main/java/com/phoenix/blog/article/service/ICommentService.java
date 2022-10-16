package com.phoenix.blog.article.service;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论信息表 服务类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 通过文章id递归查询所有评论
     *
     * @param articleId 文章id
     * @return 文章所属的所有评论
     */
    Result findByArticleId(String articleId);

    /**
     * 通过评论id递归删除评论信息
     *
     * @param id 评论id
     * @return 是否删除成功
     */
    Result deleteById(String id);
}
