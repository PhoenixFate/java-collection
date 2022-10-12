package com.phoenix.blog.article.mapper;

import com.phoenix.blog.entity.ArticleLabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章标签中间表 Mapper 接口
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Mapper
public interface ArticleLabelMapper extends BaseMapper<ArticleLabel> {

}
