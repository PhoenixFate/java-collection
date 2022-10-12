package com.phoenix.blog.article.service.impl;

import com.phoenix.blog.entity.Advert;
import com.phoenix.blog.article.mapper.AdvertMapper;
import com.phoenix.blog.article.service.IAdvertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 广告信息表 服务实现类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Service
public class AdvertServiceImpl extends ServiceImpl<AdvertMapper, Advert> implements IAdvertService {

}
