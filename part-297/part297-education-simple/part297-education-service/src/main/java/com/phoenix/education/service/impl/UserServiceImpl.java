package com.phoenix.education.service.impl;

import com.phoenix.education.model.User;
import com.phoenix.education.service.IUserService;
import com.phoenix.education.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService  {


    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public User findById(Serializable id)  {
        User user = userMapper.selectByPrimaryKey((Integer) id);
        return user;
    }

    @Override
    public int update(User user) {
        return 0;
    }

    @Override
    public int insert(User user) {
        return 0;
    }

    @Override
    public int deleteById(Serializable id) {
        return 0;
    }
}
