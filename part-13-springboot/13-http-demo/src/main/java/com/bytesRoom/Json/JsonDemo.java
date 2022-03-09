package com.bytesRoom.Json;

import com.bytesRoom.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonDemo {

    /**
     * JacksonJson是SpringMVC内置的json处理工具，其中有一个ObjectMapper类，可以方便的实现对json的处理：
     */
    // json处理工具
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 对象转json
     * @throws JsonProcessingException
     */
    @Test
    public void testObjectToJson() throws JsonProcessingException {
        User user = new User();
        user.setUserId(20);
        user.setAvatar("avatar");
        user.setUsername("abc");
        user.setSalt("911");

        // 序列化
        String json = mapper.writeValueAsString(user);
        System.out.println("json = " + json);
    }



    // json转对象
    @Test
    public void testJsonToObject() throws IOException {
        User user = new User();
        user.setUserId(20);
        user.setAvatar("avatar");
        user.setUsername("abc");
        user.setSalt("911");
        // 序列化
        String json = mapper.writeValueAsString(user);

        // 反序列化，接收两个参数：json数据，反序列化的目标类字节码
        User result = mapper.readValue(json, User.class);
        System.out.println("result = " + result);
    }


    /**
     * json数组 转 对象数组
     * @throws IOException
     */
    @Test
    public void testJsonArrayToObjectArray() throws IOException {
        User user = new User();
        user.setUserId(20);
        user.setAvatar("avatar");
        user.setUsername("abc");
        user.setSalt("911");

        // 序列化,得到对象集合的json字符串
        String json = mapper.writeValueAsString(Arrays.asList(user, user));
        System.out.println(json);

        // 反序列化，接收两个参数：json数据，反序列化的目标类字节码
        List<User> users = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        for (User u : users) {
            System.out.println("u = " + u);
        }
    }


    /**
     * json转复杂类型
     * @throws IOException
     */
    @Test
    public void testJsonToComplexObject() throws IOException {
        User user = new User();
        user.setUserId(20);
        user.setAvatar("avatar");
        user.setUsername("abc");
        user.setSalt("911");

        // 序列化,得到对象集合的json字符串
        String json = mapper.writeValueAsString(Arrays.asList(user, user));

        // 反序列化，接收两个参数：json数据，反序列化的目标类字节码
        List<User> users = mapper.readValue(json, new TypeReference<List<User>>(){});
        for (User u : users) {
            System.out.println("u = " + u);
        }
    }





}
