package com.phoenix.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.workflow.entity.ProcessConfig;
import com.phoenix.workflow.entity.SysUser;
import com.phoenix.workflow.mapper.ProcessConfigMapper;
import com.phoenix.workflow.mapper.SysUserMapper;
import com.phoenix.workflow.service.IProcessConfigService;
import com.phoenix.workflow.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/28 14:13
 */
@Service
public class ProcessConfigService extends ServiceImpl<ProcessConfigMapper, ProcessConfig> implements IProcessConfigService {

    @Override
    public ProcessConfig getByProcessKey(String processKey) {
        QueryWrapper<ProcessConfig> queryWrapper = new QueryWrapper<ProcessConfig>().eq("process_key", processKey);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Result deleteByProcessKey(String processKey) {
        QueryWrapper<ProcessConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_key", processKey);
        baseMapper.delete(queryWrapper);
        return Result.ok();
    }

    @Override
    public ProcessConfig getByBusinessRoute(String businessRoute) {
        QueryWrapper<ProcessConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("business_route", businessRoute);
        List<ProcessConfig> processConfigList = baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(processConfigList)){
            return null;
        }
        return processConfigList.get(0);
    }
}
