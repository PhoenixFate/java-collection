package com.phoenix.education.service;

import com.phoenix.education.model.User;
import com.phoenix.education.service.base.IBaseService;

public interface IUserService  extends IBaseService<User> {

//    特有的方法
    User login(String username,String password);

}
