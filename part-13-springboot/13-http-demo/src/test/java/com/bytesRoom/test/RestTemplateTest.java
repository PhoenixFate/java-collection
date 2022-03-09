package com.bytesRoom.test;

import com.bytesRoom.HttpDemoApplication;
import com.bytesRoom.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HttpDemoApplication.class)
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void httpGet() {
        // User user = this.restTemplate.getForObject("http://localhost:8088/crud/user/1", User.class);
        // System.out.println(user);
    }


}
