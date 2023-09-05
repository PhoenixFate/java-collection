package com.phoenix.education.service.base;

import com.phoenix.education.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BaseServiceImpl<T> implements IBaseService<T> {

//    统一管理dao
    @Autowired
    protected UserMapper userMapper;

}
