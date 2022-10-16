package com.phoenix.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.blog.article.mapper.CommentMapper;
import com.phoenix.blog.article.service.ICommentService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Comment;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Result findByArticleId(String articleId) {
        if (StringUtils.isBlank(articleId)) {
            return Result.error("文章id不能为空");
        }
        List<Comment> commentList = baseMapper.findByArticleId(articleId);
        return Result.ok(commentList);
    }

    @Override
    public Result deleteById(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.error("评论id不能为空");
        }
        //要删除的所有评论id（通过递归获得）
        List<String> idListToDelete = new ArrayList<>();
        idListToDelete.add(id);
        //递归所有的评论id，并将id装到要删除的集合中
        this.getAllIdListByParentId(idListToDelete, id);
        //批量删除集合中的评论id
        baseMapper.deleteBatchIds(idListToDelete);
        return Result.ok();
    }

    private void getAllIdListByParentId(List<String> idListToDelete, String parentId) {
        //查询子评论信息
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<Comment> commentList = baseMapper.selectList(queryWrapper);
        //如果子评论不为空，则取出每条评论的评论id
        if (CollectionUtils.isNotEmpty(commentList)) {
            for (Comment comment : commentList) {
                //将当前查询到的评论id放到要删除的id集合中
                idListToDelete.add(comment.getId());
                //递归继续查询子评论id
                this.getAllIdListByParentId(idListToDelete, comment.getId());
            }
        }
    }
}
