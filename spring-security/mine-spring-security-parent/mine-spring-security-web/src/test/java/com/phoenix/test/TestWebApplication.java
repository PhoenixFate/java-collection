package com.phoenix.test;

import com.phoenix.web.entity.SysUser;
import com.phoenix.web.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author phoenix
 * @Date 2022/9/19 18:08
 * @Version 1.0.0
 */
@RunWith(SpringRunner.class) //SpringBoot2.2版本之后不同加该注解
@SpringBootTest
public class TestWebApplication {

    @Autowired
    SysUserService sysUserService;

    @Test
    public void testUser() {
        List<SysUser> sysUserList = sysUserService.list();
        for (SysUser sysUser : sysUserList) {
            System.out.println(sysUser);
        }
    }

}
