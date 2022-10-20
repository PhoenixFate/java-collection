package com.phoenix.blog.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.blog.article.request.ArticleListRequest;
import com.phoenix.blog.common.request.UserInfoRequest;
import com.phoenix.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
     */
    void deleteArticleLabel(@Param("articleId") String articleId);

    /**
     * @param articleId   文章id
     * @param labelIdList 标签id数组
     */
    void saveArticleLabel(@Param("articleId") String articleId, @Param("labelIdList") List<String> labelIdList);

    /**
     * 根据标签id或者分类id查询文章列表
     *
     * @param page               分页条件
     * @param articleListRequest 查询条件
     * @return 文章列表
     */
    IPage<Article> findListByLabelIdOrCategoryId(IPage<Article> page,
                                                 @Param("articleListRequest") ArticleListRequest articleListRequest);

    /**
     * 统计每个分类下的文章数
     *
     * @return 统计数据
     */
    List<Map<String, Object>> selectCategoryTotal();

    /**
     * 统计近6个月发布的文章数
     *
     * @return 统计数据
     */
    List<Map<String, Object>> selectMonthArticleTotal();

    boolean updateUserInfo(UserInfoRequest userInfoRequest);
}
