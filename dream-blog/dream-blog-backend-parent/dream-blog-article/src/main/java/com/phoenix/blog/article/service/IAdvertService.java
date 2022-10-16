package com.phoenix.blog.article.service;

import com.phoenix.blog.article.request.AdvertRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Advert;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 广告信息表 服务类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
public interface IAdvertService extends IService<Advert> {

    /**
     * 条件分页查询广告列表
     *
     * @param advertRequest 广告查询条件
     * @return 分页后的广告列表
     */
    Result queryPage(AdvertRequest advertRequest);

    /**
     * 删除广告及OSS中的图片
     *
     * @param id 广告id
     * @return 是否删除成功
     */
    Result deleteById(String id);

    /**
     * 查询指定广告位置的所有广告信息（状态为正常）
     *
     * @param position 广告位置
     * @return 广告列表
     */
    Result findByPosition(Integer position);
}
