package com.phoenix.test;

import com.phoenix.dao.RoleDao;
import com.phoenix.dao.UserDao;
import com.phoenix.domain.Role;
import com.phoenix.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerManyToManyTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    /**
     * 多对多放弃维护权：被动的一方放弃维护权
     * 角色被选择，所以角色放弃维护权利
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToMany(){
        User user=new User();
        user.setUserName("user2");
        user.setAge(18);
        Role role=new Role();
        role.setRoleName("admin2");
        role.getUsers().add(user);
        user.getRoles().add(role);
        userDao.save(user);
        roleDao.save(role);
    }

    /**
     * 测试级联添加（保存一个用户的同时保存用户的关联角色）
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToManyCascade(){
        User user=new User();
        user.setUserName("userCascade");
        user.setAge(18);
        Role role=new Role();
        role.setRoleName("adminCascade");
        role.getUsers().add(user);
        user.getRoles().add(role);
        userDao.save(user);
    }


    /**
     * 测试级联删除
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToManyCascadeDelete(){
        User user = userDao.findOne(3L);
        userDao.delete(user);
    }




}
