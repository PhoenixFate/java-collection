package com.phoenix.blog.article.service;

import com.phoenix.blog.article.request.LabelRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Label;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
public interface ILabelService extends IService<Label> {

    Result queryPage(LabelRequest labelRequest);

    List<Label> getLabelListByIds(List<String> ids);
}
