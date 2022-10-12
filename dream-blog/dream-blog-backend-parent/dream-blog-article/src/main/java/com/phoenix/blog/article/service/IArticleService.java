package com.phoenix.blog.article.service;

import com.phoenix.blog.article.request.ArticleRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章信息表 服务类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
public interface IArticleService extends IService<Article> {

    /**
     * 条件分页查询文章列表
     *
     * @param articleRequest 文章查询条件
     * @return 文章列表
     */
    Result queryPage(ArticleRequest articleRequest);

    /**
     * 通过文章id查询文章详情及文章的标签列表
     *
     * @param id 文章id
     * @return 带标签的文章详情
     */
    Result findArticleAndLabelListById(String id);
}
