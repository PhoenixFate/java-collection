package com.phoenix.blog.question.service.impl;

import com.phoenix.blog.entity.Replay;
import com.phoenix.blog.question.mapper.ReplayMapper;
import com.phoenix.blog.question.service.IReplayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 回答信息表 服务实现类
 * </p>
 *
 * @author phoenix
 */
@Service
public class ReplayServiceImpl extends ServiceImpl<ReplayMapper, Replay> implements IReplayService {

}
