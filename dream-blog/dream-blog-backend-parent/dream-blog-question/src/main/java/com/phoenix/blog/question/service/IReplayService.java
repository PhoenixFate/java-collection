package com.phoenix.blog.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Replay;

/**
 * <p>
 * 回答信息表 服务类
 * </p>
 *
 * @author phoenix
 */
public interface IReplayService extends IService<Replay> {

    /**
     * 通过问题id递归查询所有回答信息
     *
     * @param questionId 问题id
     * @return 所有回答信息
     */
    Result findByQuestionId(String questionId);

    /**
     * 通过回答评论id递归删除
     *
     * @param id 回答评论id
     * @return 是否删除成功
     */
    Result deleteById(String id);

    /**
     * 新增回答信息及更新问题表中的回复数
     *
     * @param replay 回复
     * @return 是否新增成功
     */
    Result add(Replay replay);
}
