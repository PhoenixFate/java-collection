package com.phoenix.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.blog.article.request.LabelRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Label;
import com.phoenix.blog.article.mapper.LabelMapper;
import com.phoenix.blog.article.service.ILabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements ILabelService {

    @Override
    public Result queryPage(LabelRequest labelRequest) {
        IPage<Label> labelIPage = baseMapper.queryPage(labelRequest.getPage(), labelRequest);
        return Result.ok(labelIPage);
    }

    @Override
    public List<Label> getLabelListByIds(List<String> ids) {
        return baseMapper.getLabelListByIds(ids);
    }

    @Override
    public boolean updateById(Label label) {
        label.setUpdateDate(new Date());
        return super.updateById(label);
    }
}
