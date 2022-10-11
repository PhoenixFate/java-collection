package com.phoenix.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.blog.article.mapper.CategoryMapper;
import com.phoenix.blog.article.request.CategoryRequest;
import com.phoenix.blog.article.service.ICategoryService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 文章分类服务层
 *
 * @Author phoenix
 * @Date 2022/10/11 14:00
 * @Version 1.0.0
 */
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public Result queryPage(CategoryRequest categoryRequest) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(categoryRequest.getName())) {
            queryWrapper.like("name", categoryRequest.getName());
        }
        if (categoryRequest.getStatus() != null) {
            queryWrapper.eq("status", categoryRequest.getStatus());
        }
        queryWrapper.orderByDesc("status").orderByAsc("sort");
        //第一个参数为Page分页对象，第二个参数为查询条件
        IPage<Category> categoryIPage = baseMapper.selectPage(categoryRequest.getPage(), queryWrapper);
        return Result.ok(categoryIPage);
    }
}
