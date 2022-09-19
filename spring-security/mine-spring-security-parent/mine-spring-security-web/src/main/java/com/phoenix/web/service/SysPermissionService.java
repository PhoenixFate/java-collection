package com.phoenix.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.web.entity.SysPermission;

import java.util.List;

/**
 * @Author phoenix
 * @Date 9/19/22 23:46
 * @Version 1.0
 */
public interface SysPermissionService extends IService<SysPermission> {

    List<SysPermission> findPermissionByUserId(Long userId);

}
