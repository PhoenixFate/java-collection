package com.phoenix.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.web.entity.SysRole;
import com.phoenix.web.entity.SysUser;
import com.phoenix.web.mapper.SysRoleMapper;
import com.phoenix.web.mapper.SysUserMapper;
import com.phoenix.web.service.SysUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author phoenix
 * @Date 2022/9/19 17:49
 * @Version 1.0.0
 */
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleMapper sysRoleMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public SysUser findByUsername(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public SysUser findByMobile(String mobile) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<SysUser> selectPage(Page<SysUser> page, SysUser user) {
        return baseMapper.selectPage(page, user);
    }

    @Override
    public SysUser findById(Long userId) {
        if (userId == null) {
            return new SysUser();
        }
        //1.根据id查询用户基本信息
        SysUser sysUser = baseMapper.selectById(userId);
        //2.查询用户所拥有的角色
        List<SysRole> sysRoleList = sysRoleMapper.findListByUserId(userId);
        sysUser.setRoleList(sysRoleList);
        return sysUser;
    }

    @Transactional
    @Override
    public boolean saveOrUpdate(SysUser user) {
        if (user.getId() == null) {
            //新用户设置默认密码
            user.setPassword(passwordEncoder.encode("123456"));
        }
        user.setUpdateDate(new Date());
        boolean flag = super.saveOrUpdate(user);
        if (flag) {
            //先删除用户角色表中的数据
            baseMapper.deleteUserRoleByUserId(user.getId());
            if (CollectionUtils.isNotEmpty(user.getRoleIds())) {
                //再新增到用户角色表中
                baseMapper.saveUserRole(user.getId(), user.getRoleIds());
            }
        }

        return flag;
    }
}
