package com.phoenix.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.blog.article.request.AdvertRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.property.BlogProperty;
import com.phoenix.blog.common.util.AlibabaCloudOSSUtil;
import com.phoenix.blog.entity.Advert;
import com.phoenix.blog.article.mapper.AdvertMapper;
import com.phoenix.blog.article.service.IAdvertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 广告信息表 服务实现类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Service
@AllArgsConstructor
public class AdvertServiceImpl extends ServiceImpl<AdvertMapper, Advert> implements IAdvertService {

    private final BlogProperty blogProperty;

    @Override
    public Result queryPage(AdvertRequest advertRequest) {
        QueryWrapper<Advert> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(advertRequest.getTitle())) {
            queryWrapper.like("title", advertRequest.getTitle());
        }
        if (advertRequest.getStatus() != null) {
            queryWrapper.eq("status", advertRequest.getStatus());
        }
        IPage<Advert> advertIPage = baseMapper.selectPage(advertRequest.getPage(), queryWrapper);
        return Result.ok(advertIPage);
    }

    @Transactional
    @Override
    public Result deleteById(String id) {
        Advert advert = baseMapper.selectById(id);
        if (advert == null) {
            return Result.error("广告不存在");
        }
        //删除广告
        int i = baseMapper.deleteById(id);
        if (i > 0) {
            //删除OSS中的图片
            if (StringUtils.isNotBlank(advert.getAdvertUrl())) {
                AlibabaCloudOSSUtil.deleteOSSFile(advert.getAdvertUrl(), blogProperty.getAlibabaCloud());
            }
        }
        return Result.ok();
    }

    @Override
    public Result findByPosition(Integer position) {
        QueryWrapper<Advert> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("position", position);
        queryWrapper.eq("status", 1);
        //根据sort升序
        queryWrapper.orderByAsc("sort");
        return Result.ok(baseMapper.selectList(queryWrapper));
    }
}
