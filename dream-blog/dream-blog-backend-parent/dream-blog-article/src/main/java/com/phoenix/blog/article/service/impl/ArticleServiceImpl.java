package com.phoenix.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.blog.article.request.ArticleRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Article;
import com.phoenix.blog.article.mapper.ArticleMapper;
import com.phoenix.blog.article.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章信息表 服务实现类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Override
    public Result queryPage(ArticleRequest articleRequest) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        //标题
        if (StringUtils.isNotBlank(articleRequest.getTitle())) {
            queryWrapper.like("title", articleRequest.getTitle());
        }
        if (articleRequest.getStatus() != null) {
            queryWrapper.eq("status", articleRequest.getStatus());
        }
        queryWrapper.orderByDesc("update_date");
        IPage<Article> articleIPage = baseMapper.selectPage(articleRequest.getPage(), queryWrapper);
        return Result.ok(articleIPage);
    }

    @Override
    public Result findArticleAndLabelListById(String id) {
        Article article = baseMapper.findArticleAndLabelListById(id);
        return Result.ok(article);
    }
}
