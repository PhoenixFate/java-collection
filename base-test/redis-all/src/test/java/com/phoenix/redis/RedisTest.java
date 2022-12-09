package com.phoenix.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author phoenix
 * @Date 2022/12/9 12:22
 * @Version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {


    // 注入 RedisTemplate
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void test01() {
        redisTemplate.opsForValue().set("abc", "bbb");
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println("key: " + key + " value: " + redisTemplate.opsForValue().get("aaa"));
        }
        //设置过期时间
        redisTemplate.opsForValue().set("ccc", "ccc", 5, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("ddd", "ddd");
        redisTemplate.expire("ddd", 5, TimeUnit.SECONDS);
        //获取过期时间
        Long expire2 = redisTemplate.getExpire("ddd");
        System.out.println("ddd 的过期时间： " + expire2);

        //同时设置多个key
        Map<String, String> stringMap = new LinkedHashMap<>();
        stringMap.put("java-1", "java-1");
        stringMap.put("java-2", "java-2");
        redisTemplate.opsForValue().multiSet(stringMap);

        List<String> stringList = redisTemplate.opsForValue().multiGet(stringMap.keySet());
        for (String s : stringList) {
            System.out.println(s);
        }

        redisTemplate.opsForValue().set("int-number", "5");
        redisTemplate.opsForValue().increment("int-number",10);
        System.out.println(redisTemplate.opsForValue().get("int-number"));
    }

    /**
     * String 类型操作
     */
    @Test
    public void StringMethod() {
        // 查询所有
        System.out.println("=========[查询所有]=========");
        Set<String> keys = redisTemplate.keys("jedis*");
        for (String key : keys) {
            System.out.println("键：" + key + ", 键的类型：" + redisTemplate.opsForValue().get(key) + ", 值：" + redisTemplate.opsForValue().get(key));
        }
        // 设置键值对
        System.out.println("=========[设置键值对]=========");
        redisTemplate.opsForValue().set("jedis1", "1");
        redisTemplate.opsForValue().set("jedis2", "2");
        redisTemplate.opsForValue().set("jedis3", "3");
        keys = redisTemplate.keys("jedis*");
        for (String key : keys) {
            System.out.println("键：" + key + ", 键的类型：" + redisTemplate.opsForValue().get(key) + ", 值：" + redisTemplate.opsForValue().get(key));
        }
        // 设置有效期限的键，获取 ttl 值
        System.out.println("=========[设置有效期限的键，获取 ttl 值]=========");
        redisTemplate.opsForValue().set("expire1", "ttl key");
        redisTemplate.expire("expire1", 60, TimeUnit.SECONDS);
        try {
            Thread.sleep(5000); // 休眠 5 秒钟
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("休眠 5 秒钟之后，ttl 值为：" + redisTemplate.getExpire("expire1"));
        // 同时设置多个键值对，并获取
        System.out.println("=========[同时设置多个键值对，并获取]=========");
        Map<String, String> stringStringMap = new LinkedHashMap<>();
        stringStringMap.put("java-1", "6");
        stringStringMap.put("java-2", "8");
        stringStringMap.put("java-3", "10");
        redisTemplate.opsForValue().multiSet(stringStringMap);
        List<String> multiGet = redisTemplate.opsForValue().multiGet(stringStringMap.keySet());
        System.out.println("同时获取多个键的值：" + multiGet.toString());
        // 对 数值型的 键值对进行增减操作
        System.out.println("=========[对 数值型的 键值对进行增减操作]=========");
        redisTemplate.opsForValue().increment("java-1", 3L);
        redisTemplate.opsForValue().decrement("java-3", 5L);
        multiGet = redisTemplate.opsForValue().multiGet(stringStringMap.keySet());
        System.out.println("同时获取多个键的值：" + multiGet.toString());
        // 返回旧的数据，更新新的数据
        System.out.println("=========[返回旧的数据，更新新的数据]=========");
        String andSet = redisTemplate.opsForValue().getAndSet("java-2", "new-value");
        System.out.println("java-2 键的原来的值：" + andSet + ", 新的值：" + redisTemplate.opsForValue().get("java-2"));
        // 对值进行字符串操作
        System.out.println("=========[对值进行字符串操作]=========");
        redisTemplate.opsForValue().append("java-2", "-aaa");
        System.out.println("java-2 键的原来的值：" + andSet + ", 新的值：" + redisTemplate.opsForValue().get("java-2"));
        // 删除元素
        System.out.println("=========[删除元素]=========");
        Set<String> keys1 = redisTemplate.keys("*");
        redisTemplate.delete(keys1);
    }


}
