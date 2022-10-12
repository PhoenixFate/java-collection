package com.phoenix.blog.article.service.impl;

import com.phoenix.blog.entity.ArticleLabel;
import com.phoenix.blog.article.mapper.ArticleLabelMapper;
import com.phoenix.blog.article.service.IArticleLabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签中间表 服务实现类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Service
public class ArticleLabelServiceImpl extends ServiceImpl<ArticleLabelMapper, ArticleLabel> implements IArticleLabelService {

}
