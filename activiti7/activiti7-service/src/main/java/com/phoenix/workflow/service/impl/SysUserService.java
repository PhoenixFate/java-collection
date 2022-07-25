package com.phoenix.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.workflow.entity.SysUser;
import com.phoenix.workflow.mapper.SysUserMapper;
import com.phoenix.workflow.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Override
    public SysUser findByUserName(String userName) {
        if(StringUtils.isEmpty(userName)){
            return null;
        }
        QueryWrapper<SysUser> wrapper=new QueryWrapper<>();
        wrapper.eq("username",userName);
        return baseMapper.selectOne(wrapper);
    }
}
