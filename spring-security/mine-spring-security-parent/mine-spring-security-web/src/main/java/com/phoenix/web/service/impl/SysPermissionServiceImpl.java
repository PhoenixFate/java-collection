package com.phoenix.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.mapper.SysPermissionMapper;
import com.phoenix.web.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author phoenix
 * @Date 9/19/22 23:46
 * @Version 1.0
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    @Override
    public List<SysPermission> findPermissionByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return baseMapper.selectPermissionByUserId(userId);
    }
}
