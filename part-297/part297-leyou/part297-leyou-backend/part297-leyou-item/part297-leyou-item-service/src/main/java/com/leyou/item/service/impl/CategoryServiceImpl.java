package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import com.leyou.myexception.ExceptionEnum;
import com.leyou.myexception.LeyouException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoryListByPid(Long pid) {

        Category category=new Category();
        category.setParentId(pid);
        List<Category> categoryList = categoryMapper.select(category);
        // 在restful风格中，如果没有查到要返回404
//        if(CollectionUtils.isEmpty(categoryList)){
//            throw new LeyouException(ExceptionEnum.CATEGORY_NOT_FOUND);
//        }
        return categoryList;
    }
}
