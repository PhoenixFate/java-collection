package com.phoenix.blog.article.mapper;

import com.phoenix.blog.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 通过文章id查询文章详情及文章的标签列表
     *
     * @param id 文章id
     * @return 文章详情
     */
    Article findArticleAndLabelListById(String id);
}
