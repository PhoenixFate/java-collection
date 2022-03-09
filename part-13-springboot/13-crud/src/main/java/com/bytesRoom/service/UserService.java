package com.bytesRoom.service;

import com.bytesRoom.mapper.UserMapper;
import com.bytesRoom.pojo.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public User findByQueryById(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public void insertUser(User user){
        userMapper.insert(user);
    }

    public List<User> getAllUser(){
        return userMapper.selectAll();
    }
}
