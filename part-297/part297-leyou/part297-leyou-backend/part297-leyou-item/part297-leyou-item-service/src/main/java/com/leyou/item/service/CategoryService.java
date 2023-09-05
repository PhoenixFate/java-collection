package com.leyou.item.service;

import java.util.List;
import com.leyou.item.pojo.Category;

public interface CategoryService {
    List<Category> queryCategoryListByPid(Long pid);
}
