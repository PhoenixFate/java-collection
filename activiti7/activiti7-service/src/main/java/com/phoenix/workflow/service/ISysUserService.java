package com.phoenix.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.workflow.entity.SysUser;

public interface ISysUserService extends IService<SysUser> {

    SysUser findByUserName(String userName);

}
