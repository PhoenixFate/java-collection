package com.leyou.item.service;


import com.leyou.item.pojo.Brand;
import com.leyou.vo.PageResult;

import java.util.List;

public interface BrandService {
    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean descending, String searchValue);

    void saveBrand(Brand brand, List<Long> cids);
}
