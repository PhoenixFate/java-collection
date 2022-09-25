package com.phoenix.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.entity.SysRole;
import com.phoenix.web.mapper.SysPermissionMapper;
import com.phoenix.web.mapper.SysRoleMapper;
import com.phoenix.web.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author phoenix
 * @Date 9/19/22 22:13
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysPermissionMapper sysPermissionMapper;

    @Override
    public IPage<SysRole> selectPage(Page<SysRole> page, SysRole sysRole) {
        return baseMapper.selectPage(page, sysRole);
    }

    @Override
    public SysRole findById(Long roleId) {
        //1.通过角色id查询对应的角色信息
        SysRole sysRole = baseMapper.selectById(roleId);
        //2.通过角色id查询角色所拥有的所有权限
        List<SysPermission> sysPermissionList = sysPermissionMapper.findByRoleId(roleId);
        //3.将查询到的权限set到角色对应的SysRole中
        sysRole.setPermissionList(sysPermissionList);
        return sysRole;
    }

    @Transactional
    @Override
    public boolean deleteById(Long roleId) {
        boolean flag = baseMapper.deleteById(roleId) > 0;
        if (flag) {
            baseMapper.deleteRolePermissionByRoleId(roleId);
        }
        return flag;
    }

    @Transactional //开启事务管理
    @Override
    public boolean saveOrUpdate(SysRole sysRole) {
        sysRole.setUpdateDate(new Date());
        //1.更新角色表中的内容
        boolean flag = super.saveOrUpdate(sysRole);
        if (flag) {
            //2.更新角色权限关系中的数据
            baseMapper.deleteRolePermissionByRoleId(sysRole.getId());
            //3.更新角色权限关系表中的数据
            if (CollectionUtils.isNotEmpty(sysRole.getPermissionIds())) {
                baseMapper.saveRolePermission(sysRole.getId(), sysRole.getPermissionIds());
            }
        }
        return flag;
    }
}
