package com.phoenix.redis;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
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
        redisTemplate.opsForValue().increment("int-number", 10);
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

    @Test
    public void test02() {
        redisTemplate.opsForList().leftPush("list01", "list01");
        redisTemplate.opsForList().leftPush("list01", "list02");

        List<String> leftNameList = new LinkedList<>();
        leftNameList.add("mike");
        leftNameList.add("peter");
        redisTemplate.opsForList().leftPushAll("left-push-name", leftNameList);

        String index = redisTemplate.opsForList().index("left-push-name", 0);
        System.out.println("根据索引获得元素 " + index);

        List<String> range = redisTemplate.opsForList().range("left-push-name", 0, -1);
        System.out.println(JSON.toJSONString(range));
    }

    /**
     * List 类型操作
     */
    public void ListMethod() {
        // 从左侧和右侧 添加元素
        System.out.println("=========[从左侧和右侧 添加元素]=========");
        List<String> nameList = new LinkedList<>();
        nameList.add("Mike");
        nameList.add("Peter");
        nameList.add("Jack");
        redisTemplate.opsForList().leftPushAll("list-java-1", nameList); // 左侧 push
        nameList.clear();
        nameList.add("aaa");
        nameList.add("bbb");
        nameList.add("ccc");
        redisTemplate.opsForList().rightPushAll("list-java-1", nameList); // 右侧 push
        // 根据索引获取元素
        System.out.println("=========[根据索引获取元素]=========");
        String index = redisTemplate.opsForList().index("list-java-1", 1);
        System.out.println("索引为 1 的元素为：" + index);
        // 获取所有元素
        System.out.println("=========[获取所有元素]=========");
        List<String> range = redisTemplate.opsForList().range("list-java-1", 0, -1);
        System.out.println("所有元素：" + range.toString());
        // 删除指定数量的某元素
        System.out.println("=========[删除指定数量的某元素]=========");
        redisTemplate.opsForList().remove("list-java-1", 2, "Mike");
        range = redisTemplate.opsForList().range("list-java-1", 0, -1);
        System.out.println("元素数量：" + redisTemplate.opsForList().size("list-java-1") + "， 所有元素：" + range.toString());
        // 删除全部元素
        System.out.println("=========[删除全部元素]=========");
        redisTemplate.opsForList().trim("list-java-1", 0, 0);
        redisTemplate.opsForList().leftPop("list-java-1");
        range = redisTemplate.opsForList().range("list-java-1", 0, -1);
        System.out.println("元素数量：" + redisTemplate.opsForList().size("list-java-1") + "， 所有元素：" + range.toString());
    }

    @Test
    public void test00() {
        Map<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put("bbb", "value1");
        hashMap.put("qwerq", "value2");
        hashMap.put("aaa", "value3");
        Set<Map.Entry<String, String>> set = hashMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("key:" + key + ", value:" + value);
        }

    }


    @Test
    public void test05() {
        List<User> userList = new LinkedList<>();
        userList.add(new User(2022090101, "Mike", 21, "Beijing"));
        userList.add(new User(2022090102, "Peter", 24, "Shanghai"));
        userList.add(new User(2022090103, "Jack", 22, "Shenzhen"));
        userList.add(new User(2022090104, "云梦", 22, "Dalian"));
        userList.add(new User(2022090105, "归遥", 20, "Hangzhou"));
        Map<String, String> userMap = new LinkedHashMap<>();
        for (User user : userList) {
            userMap.put(String.valueOf(user.getId()), JSON.toJSONString(user));
        }
        redisTemplate.delete("user-hash");
        redisTemplate.opsForHash().putAll("user-hash", userMap);
        // 获取所有的键，值
        System.out.println("============[获取所有的键，值]===========");
        Set<Object> keys = redisTemplate.opsForHash().keys("user-hash");
        List<Object> values = redisTemplate.opsForHash().values("user-hash");
        System.out.println("所有的键：" + keys.toString());
        System.out.println("所有的值：" + values.toString());
    }


    /**
     * Hash 类型方法
     */
    public void HashMethod() {
        // 模拟从数据库中读取大量用户数据，缓存到 redis 中
        List<User> userList = new LinkedList<>();
        userList.add(new User(2022090101, "Mike", 21, "Beijing"));
        userList.add(new User(2022090102, "Peter", 24, "Shanghai"));
        userList.add(new User(2022090103, "Jack", 22, "Shenzhen"));
        userList.add(new User(2022090104, "云梦", 22, "Dalian"));
        userList.add(new User(2022090105, "归遥", 20, "Hangzhou"));
        // 将数据库中读取出来的数据进行处理，构建为 map
        Map<String, String> userMap = new LinkedHashMap<>();
        for (User user : userList) {
            userMap.put(String.valueOf(user.getId()), JSON.toJSONString(user));
        }
        // 增加数据（进行序列化）
        System.out.println("============[增加数据（进行序列化）]===========");
        redisTemplate.opsForHash().putAll("hash-java-1", userMap);
        // 获取所有的键，值
        System.out.println("============[获取所有的键，值]===========");
        Set<Object> keys = redisTemplate.opsForHash().keys("hash-java-1");
        List<Object> values = redisTemplate.opsForHash().values("hash-java-1");
        System.out.println("所有的键：" + keys.toString());
        System.out.println("所有的值：" + values.toString());
        // 判断某个键是否存在
        System.out.println("============[判断某个键是否存在]===========");
        Boolean aBoolean = redisTemplate.opsForHash().hasKey("hash-java-1", "2022090101");
        System.out.println("2022090101 是否存在：" + aBoolean);
        aBoolean = redisTemplate.opsForHash().hasKey("hash-java-1", "2022090109");
        System.out.println("2022090109 是否存在：" + aBoolean);
        // 获取并修改某个键对应的值
        System.out.println("============[获取并修改某个键对应的值]===========");
        if (redisTemplate.opsForHash().hasKey("hash-java-1", "2022090101")) {
            String userString = (String) redisTemplate.opsForHash().get("hash-java-1", "2022090101");
            User user = JSON.parseObject(userString, User.class);
            System.out.println("2022090101 对应的数据为：" + user.toString());
            user.setAge(30);
            user.setAddress("Guangzhou");
            redisTemplate.opsForHash().put("hash-java-1", "2022090101", JSON.toJSONString(user));
        } else {
            System.out.println("2022090101 对应的数据为 null");
        }
        // 获取所有键值对
        System.out.println("============[获取所有键值对]===========");
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("hash-java-1");
        System.out.println("元素数量：" + entries.size());
        Set<Map.Entry<Object, Object>> entries1 = entries.entrySet();
        for (Map.Entry<Object, Object> entry : entries1) {
            System.out.println("键：" + entry.getKey() + ", 值：" + entry.getValue());
        }
        // 删除元素
        System.out.println("============[删除元素]===========");
        redisTemplate.opsForHash().keys("hash-java-1").forEach(key -> redisTemplate.opsForHash().delete("hash-java-1", key));
    }


}
