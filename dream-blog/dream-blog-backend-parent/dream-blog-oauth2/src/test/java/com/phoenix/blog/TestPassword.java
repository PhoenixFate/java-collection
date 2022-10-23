package com.phoenix.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author phoenix
 * @Date 10/22/22 17:47
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPassword {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test01() {
        System.out.println(passwordEncoder.encode("123456"));
    }

}
