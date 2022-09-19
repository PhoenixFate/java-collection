package com.phoenix.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.web.entity.SysUser;
import com.phoenix.web.mapper.SysUserMapper;
import com.phoenix.web.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @Author phoenix
 * @Date 2022/9/19 17:49
 * @Version 1.0.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser findByUsername(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }
}
