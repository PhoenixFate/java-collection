package com.phoenix.blog.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.entity.Replay;

import java.util.List;

/**
 * <p>
 * 回答信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 */
public interface ReplayMapper extends BaseMapper<Replay> {

    /**
     * 通过问题id递归查询所有回答信息
     *
     * @return 所有回答信息
     */
    List<Replay> findByQuestionId(String questionId);
}
