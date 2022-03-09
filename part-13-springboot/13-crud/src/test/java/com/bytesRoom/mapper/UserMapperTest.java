package com.bytesRoom.mapper;

import com.bytesRoom.CrudApplication;
import com.bytesRoom.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrudApplication.class)
@Slf4j
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    //事务
    @Transactional
    public void testQuery(){
        User user = userMapper.selectByPrimaryKey(12);
        //log.info(user.toString());
        System.out.println(user);
        List<User> users = userMapper.selectAll();
        System.out.println(users);

    }
}