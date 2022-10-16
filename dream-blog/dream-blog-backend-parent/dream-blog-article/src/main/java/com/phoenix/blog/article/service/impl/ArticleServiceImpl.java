package com.phoenix.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.blog.article.dto.ArticleCategoryItemDTO;
import com.phoenix.blog.article.dto.ArticleCategoryTotalDTO;
import com.phoenix.blog.article.request.ArticleListRequest;
import com.phoenix.blog.article.request.ArticleRequest;
import com.phoenix.blog.article.request.ArticleUserRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.constant.ArticleStatusEnum;
import com.phoenix.blog.entity.Article;
import com.phoenix.blog.article.mapper.ArticleMapper;
import com.phoenix.blog.article.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
        queryWrapper.eq("status", ArticleStatusEnum.SUCCESS.getCode());
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

    @Transactional
    @Override
    public Result updateOrSave(Article article) {
        //1.如果id不为空，则为更新
        if (StringUtils.isNotBlank(article.getId())) {
            //更新：先删除文章中间数据，再新增到中间表
            baseMapper.deleteArticleLabel(article.getId());
            //将更新时间重新设置
            article.setUpdateDate(new Date());
        }
        //如果文章是不公开的，直接审核通过，否则等待审核
        //0不公开、1公开
        if (article.getIsPublic() == 0) {
            article.setStatus(ArticleStatusEnum.SUCCESS.getCode());
        } else {
            article.setStatus(ArticleStatusEnum.WAIT.getCode());
        }
        //更新或者保存文章信息
        super.saveOrUpdate(article);
        //新增标签数据到文章标签中间表中
        if (CollectionUtils.isNotEmpty(article.getLabelIdList())) {
            baseMapper.saveArticleLabel(article.getId(), article.getLabelIdList());
        }
        return Result.ok(article.getId());
    }

    @Override
    public Result updateStatus(String articleId, ArticleStatusEnum articleStatusEnum) {
        //先查询当前数据库中的数据
        Article article = baseMapper.selectById(articleId);
        //将状态值更新
        article.setStatus(articleStatusEnum.getCode());
        article.setUpdateDate(new Date());
        baseMapper.updateById(article);
        return Result.ok();
    }

    @Override
    public Result findListByUserId(ArticleUserRequest articleUserRequest) {
        if (StringUtils.isEmpty(articleUserRequest.getUserId())) {
            return Result.error("无效的用户id");
        }
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", articleUserRequest.getUserId());
        if (articleUserRequest.getIsPublic() != null) {
            queryWrapper.eq("is_public", articleUserRequest.getIsPublic());
        }
        queryWrapper.orderByDesc("update_date");
        IPage<Article> articleIPage = baseMapper.selectPage(articleUserRequest.getPage(), queryWrapper);
        return Result.ok(articleIPage);
    }

    @Override
    public Result updateLikesNumber(String articleId, Integer likesNumber) {
        if (likesNumber != -1 && likesNumber != 1) {
            return Result.error("无效操作！");
        }
        if (StringUtils.isEmpty(articleId)) {
            return Result.error("无效操作！");
        }
        //查询现有点赞数，查询到之后，将点赞数进行更新
        Article article = baseMapper.selectById(articleId);
        if (article == null) {
            return Result.error("文章不存在");
        }
        if (article.getLikesNumber() <= 0 && likesNumber == -1) {
            return Result.error("无效操作");
        }
        //更新点赞数
        article.setLikesNumber(article.getLikesNumber() + likesNumber);
        baseMapper.updateById(article);
        return Result.ok();
    }

    @Override
    public Result updateViewCount(String articleId) {
        if (StringUtils.isBlank(articleId)) {
            return Result.error("无效操作");
        }
        Article article = baseMapper.selectById(articleId);
        if (article == null) {
            return Result.error("文章不存在");
        }
        article.setViewCount(article.getViewCount() + 1);
        baseMapper.updateById(article);
        return Result.ok();
    }

    @Override
    public Result findListByLabelIdOrCategoryId(ArticleListRequest articleListRequest) {
        IPage<Article> articleIPage = baseMapper.findListByLabelIdOrCategoryId(articleListRequest.getPage(), articleListRequest);
        return Result.ok(articleIPage);
    }

    @Override
    public Result getArticleTotal() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        //状态是审核通过的
        queryWrapper.eq("status", ArticleStatusEnum.SUCCESS.getCode());
        //文章是公开的
        queryWrapper.eq("is_public", 1);
        Integer total = baseMapper.selectCount(queryWrapper);
        return Result.ok(total);
    }

    @Override
    public Result selectCategoryTotal() {
        List<Map<String, Object>> mapList = baseMapper.selectCategoryTotal();
        //将分类名称单独提取到集合中
        List<String> nameList = new ArrayList<>();
        List<ArticleCategoryItemDTO> articleCategoryItemDTOList = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : mapList) {
            nameList.add((String) stringObjectMap.get("name"));
            ArticleCategoryItemDTO articleCategoryItemDTO = new ArticleCategoryItemDTO();
            articleCategoryItemDTO.setName((String) stringObjectMap.get("name"));
            articleCategoryItemDTO.setValue(((BigDecimal) stringObjectMap.get("categoryTotal")));
            articleCategoryItemDTOList.add(articleCategoryItemDTO);
        }
        ArticleCategoryTotalDTO articleCategoryTotalDTO = new ArticleCategoryTotalDTO();
        articleCategoryTotalDTO.setNameList(nameList).setNameAndValueList(articleCategoryItemDTOList);
        return Result.ok(articleCategoryTotalDTO);
    }

    @Override
    public Result selectMonthArticleTotal() {
        List<Map<String, Object>> mapList = baseMapper.selectMonthArticleTotal();
        //将年月提取到集合中
        List<Object> yearMonthList = new ArrayList<>();
        //将每个月的文章数提取到集合中
        List<Object> articleTotalList = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : mapList) {
            yearMonthList.add(stringObjectMap.get("yearMonth"));
            articleTotalList.add(stringObjectMap.get("total"));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("yearMonthList", yearMonthList);
        data.put("articleTotalList", articleTotalList);
        return Result.ok(data);
    }
}
