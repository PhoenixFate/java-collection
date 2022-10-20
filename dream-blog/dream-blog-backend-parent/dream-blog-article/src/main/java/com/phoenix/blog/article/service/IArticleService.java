package com.phoenix.blog.article.service;

import com.phoenix.blog.article.request.ArticleListRequest;
import com.phoenix.blog.article.request.ArticleRequest;
import com.phoenix.blog.article.request.ArticleUserRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.constant.ArticleStatusEnum;
import com.phoenix.blog.common.request.UserInfoRequest;
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

    /**
     * 新增或者修改文章信息
     *
     * @return 返回文章id，前端拿着文章id进行跳转
     */
    Result updateOrSave(Article article);

    /**
     * 修改文章状态
     *
     * @param articleId         文章id
     * @param articleStatusEnum 文章枚举状态
     * @return 是否修改成功
     */
    Result updateStatus(String articleId, ArticleStatusEnum articleStatusEnum);

    /**
     * 根据用户id查询公开或者不公开的文章列表
     *
     * @param articleUserRequest 带用户id和是否公开的带分页的请求参数
     * @return 属于某用户的文章列表
     */
    Result findListByUserId(ArticleUserRequest articleUserRequest);

    /**
     * 更新点赞数
     *
     * @param articleId   文章id
     * @param likesNumber 点赞数
     * @return 更新成功
     */
    Result updateLikesNumber(String articleId, Integer likesNumber);

    /**
     * 更新文章浏览次数
     *
     * @param articleId 文章Id
     * @return 是否更新成功
     */
    Result updateViewCount(String articleId);

    /**
     * 根据标签id或者分类id查询文章列表
     *
     * @param articleListRequest 查询条件
     * @return 文章列表
     */
    Result findListByLabelIdOrCategoryId(ArticleListRequest articleListRequest);

    /**
     * 查询文章总数
     *
     * @return 文章总数
     */
    Result getArticleTotal();

    /**
     * 统计每个分类下的文章数据
     *
     * @return 统计数据
     */
    Result selectCategoryTotal();

    /**
     * 统计近6个月发布的文章数
     *
     * @return 统计数据
     */
    Result selectMonthArticleTotal();

    boolean updateUserInfo(UserInfoRequest userInfoRequest);
}
