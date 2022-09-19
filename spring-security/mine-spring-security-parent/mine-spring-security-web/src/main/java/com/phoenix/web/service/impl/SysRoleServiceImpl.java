package com.phoenix.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.web.entity.SysRole;
import com.phoenix.web.mapper.SysRoleMapper;
import com.phoenix.web.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * @Author phoenix
 * @Date 9/19/22 22:13
 * @Version 1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
