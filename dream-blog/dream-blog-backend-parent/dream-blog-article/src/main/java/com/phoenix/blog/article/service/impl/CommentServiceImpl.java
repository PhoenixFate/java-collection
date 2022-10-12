package com.phoenix.blog.article.service.impl;

import com.phoenix.blog.entity.Comment;
import com.phoenix.blog.article.mapper.CommentMapper;
import com.phoenix.blog.article.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论信息表 服务实现类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
