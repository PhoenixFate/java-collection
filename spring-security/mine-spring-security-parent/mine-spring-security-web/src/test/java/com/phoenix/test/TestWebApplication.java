package com.phoenix.test;

import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.entity.SysRole;
import com.phoenix.web.entity.SysUser;
import com.phoenix.web.service.SysPermissionService;
import com.phoenix.web.service.SysRoleService;
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
//在SpringBoot2.2.0以前是JUnit4，在SpringBoot之后是JUnit5；JUnit5不需要@RunWith(SpringRunner.class)
@RunWith(SpringRunner.class) //SpringBoot2.2版本之后不用加该注解
@SpringBootTest
public class TestWebApplication {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Test
    public void testUser() {
        List<SysUser> sysUserList = sysUserService.list();
        for (SysUser sysUser : sysUserList) {
            System.out.println(sysUser);
        }
    }

    @Test
    public void testFindUserByUsername() {
        SysUser admin = sysUserService.findByUsername("admin");
        System.out.println(admin);
    }

    @Test
    public void testSysRole() {
        SysRole sysRole = sysRoleService.getById(9);
        System.out.println(sysRole);
    }

    @Test
    public void testSysPermission() {
        SysPermission sysPermission = sysPermissionService.getById(11);
        System.out.println(sysPermission);
    }

    @Test
    public void testFindPermissionByUserId() {
        List<SysPermission> sysPermissionList = sysPermissionService.findPermissionByUserId(20L);
        System.out.println(sysPermissionList.size());
        for (SysPermission sysPermission : sysPermissionList) {
            System.out.println(sysPermission);
        }
    }

}
