package com.phoenix.workflow.test;

import com.phoenix.workflow.entity.SysUser;
import com.phoenix.workflow.mapper.SysUserMapper;
import com.phoenix.workflow.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestUser {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void testUserMapper(){
        SysUser sysUser=new SysUser();
        sysUser.setUsername("test");
        sysUser.setNickName("测试账户");
        sysUserMapper.insert(sysUser);
    }

    @Test
    public void testUserService(){
        SysUser meng = sysUserService.findByUserName("meng");
        log.info("user: {}", ToStringBuilder.reflectionToString(meng, ToStringStyle.JSON_STYLE));
    }

}
