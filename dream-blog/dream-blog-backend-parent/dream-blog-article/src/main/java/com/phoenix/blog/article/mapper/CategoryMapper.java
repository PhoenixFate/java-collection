package com.phoenix.blog.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章分类表 Mapper 接口
 *
 * @Author phoenix
 * @Date 2022/10/11 13:33
 * @Version 1.0.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
