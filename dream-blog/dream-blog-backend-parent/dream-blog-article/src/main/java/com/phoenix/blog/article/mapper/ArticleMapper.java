package com.phoenix.blog.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 通过文章id删除文章标签中间表
     *
     * @param articleId 文章id
     * @return 是否删除成功
     */
    boolean deleteArticleLabel(@Param("articleId") String articleId);

    /**
     * @param articleId   文章id
     * @param labelIdList 标签id数组
     * @return 是否新增成功
     */
    boolean saveArticleLabel(@Param("articleId") String articleId, @Param("labelIdList") List<String> labelIdList);
}
