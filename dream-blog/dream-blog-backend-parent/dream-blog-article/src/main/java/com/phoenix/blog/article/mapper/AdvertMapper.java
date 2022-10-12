package com.phoenix.blog.article.mapper;

import com.phoenix.blog.entity.Advert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 广告信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Mapper
public interface AdvertMapper extends BaseMapper<Advert> {

}
