package com.phoenix.oauth2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author phoenix
 * @Date 10/3/22 23:32
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAuthApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncodePassword() {
        System.out.println(passwordEncoder.encode("pc-secret"));
    }


}
