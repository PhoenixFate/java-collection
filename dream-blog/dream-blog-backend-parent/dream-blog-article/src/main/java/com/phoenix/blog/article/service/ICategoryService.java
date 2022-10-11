package com.phoenix.blog.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.blog.article.request.CategoryRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Category;

/**
 * 文章分类service接口
 *
 * @Author phoenix
 * @Date 2022/10/11 13:59
 * @Version 1.0.0
 */
public interface ICategoryService extends IService<Category> {
    /**
     * 分页查询分类信息
     *
     * @param categoryRequest 文章分页请求参数
     * @return 带分页的result对象
     */
    Result queryPage(CategoryRequest categoryRequest);
}
