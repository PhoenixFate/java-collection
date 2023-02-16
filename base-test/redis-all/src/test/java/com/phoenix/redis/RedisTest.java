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
     * Set 集合类型方法
     */
    @Test
    public void SetMethod(){
        // 添加元素
        System.out.println("=========[添加元素]=========");
        redisTemplate.opsForSet().add("set-java-1", "Mike", "Peter", "Jack", "Mike", "Smith");
        redisTemplate.opsForSet().add("set-java-2", "Peter", "Jordan", "Amy", "Smith", "Wof");
        // 查询元素
        System.out.println("=========[查询元素]=========");
        Set<String> members = redisTemplate.opsForSet().members("set-java-1");
        Set<String> members2 = redisTemplate.opsForSet().members("set-java-2");
        System.out.println("元素数量：" + redisTemplate.opsForSet().size("set-java-1") + "， set-java-1 的所有元素：" + members.toString());
        System.out.println("元素数量：" + redisTemplate.opsForSet().size("set-java-2") + "， set-java-2 的所有元素：" + members2.toString());
        // 判断元素是否在 set 中
        System.out.println("=========[判断元素是否在 set 中]=========");
        System.out.println(redisTemplate.opsForSet().isMember("set-java-1", "Mike")? "Mike 存在": "Mike 不存在");
        System.out.println(redisTemplate.opsForSet().isMember("set-java-2", "Jorge")? "Jorge 存在": "Jorge 不存在");
        // 交集，并集，差集的计算
        System.out.println("=========[交集，并集，差集的计算]=========");
        Set<String> intersect = redisTemplate.opsForSet().intersect("set-java-1", "set-java-2");
        Set<String> union = redisTemplate.opsForSet().union("set-java-1", "set-java-2");
        Set<String> difference = redisTemplate.opsForSet().difference("set-java-1", "set-java-2");
        System.out.println("交集：" + intersect.toString());
        System.out.println("并集：" + union.toString());
        System.out.println("差集：" + difference.toString());
        // 删除元素
        System.out.println("=========[删除元素]=========");
        redisTemplate.opsForSet().remove("set-java-1", "Mike");
        redisTemplate.opsForSet().move("set-java-1", "set-java-2", "Jack");
        redisTemplate.opsForSet().pop("set-java-1", redisTemplate.opsForSet().size("set-java-1"));
        redisTemplate.opsForSet().pop("set-java-2", redisTemplate.opsForSet().size("set-java-2"));
    }


    /**
     * Sort Set 类型方法（有序集合）
     */
    @Test
    public void SortSetMethod(){
        // 添加元素
        System.out.println("=========[添加元素]=========");
        redisTemplate.opsForZSet().add("sortset-java-1", "不及格", 59.0);
        redisTemplate.opsForZSet().add("sortset-java-1", "及格", 69.0);
        redisTemplate.opsForZSet().add("sortset-java-1", "中等", 79.0);
        redisTemplate.opsForZSet().add("sortset-java-1", "良好", 89.0);
        redisTemplate.opsForZSet().add("sortset-java-1", "优秀", 99.0);
        redisTemplate.opsForZSet().add("sortset-java-1", "满分", 100.0);
        // 查询元素
        System.out.println("=========[查询元素]=========");
        System.out.println("元素总数为：" + redisTemplate.opsForZSet().size("sortset-java-1"));
        System.out.println("优秀分数为：" + redisTemplate.opsForZSet().score("sortset-java-1", "优秀"));
        System.out.println("及格人数为：" + redisTemplate.opsForZSet().count("sortset-java-1", 60, 100) + ", 上面的档次有：" + redisTemplate.opsForZSet().rangeByScore("sortset-java-1", 60, 100).toString());
        System.out.println("中等人数为：" + redisTemplate.opsForZSet().count("sortset-java-1", 70, 100) + ", 上面的档次有：" + redisTemplate.opsForZSet().rangeByScore("sortset-java-1", 70, 100).toString());
        System.out.println("良好人数为：" + redisTemplate.opsForZSet().count("sortset-java-1", 80, 100) + ", 上面的档次有：" + redisTemplate.opsForZSet().rangeByScore("sortset-java-1", 80, 100).toString());
        System.out.println("优秀人数为：" + redisTemplate.opsForZSet().count("sortset-java-1", 90, 100) + ", 上面的档次有：" + redisTemplate.opsForZSet().rangeByScore("sortset-java-1", 90, 100).toString());
        // 删除元素
        System.out.println("=========[删除元素]=========");
        //redisTemplate.opsForZSet().remove("sortset-java-1", "不及格"); // 单独删除一个元素
        //redisTemplate.opsForZSet().removeRangeByScore("sortset-java-1", 60, 100); // 删除分数段的所有元素
    }




    /**
     * Hash 类型方法
     */
    @Test
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
     * 事务
     */
    public void MultiMethod(){
        // 模拟转账操作（模拟 Mike，Jack用户各有 100 元）
        redisTemplate.opsForValue().set("Mike", "100");
        redisTemplate.opsForValue().set("Jack", "100");
        // 进行转账操作（转账 20 元）
        int transfer = 20;
        String mike = redisTemplate.opsForValue().get("Mike");
        redisTemplate.setEnableTransactionSupport(true); // 开启事务支持
        redisTemplate.watch(Arrays.asList("Mike", "Jack")); // 开启监听
        redisTemplate.multi(); // 开启事务
        try{
            // 判断余额是否足够
            if (Integer.parseInt(mike) >= transfer){
                redisTemplate.opsForValue().decrement("Mike", (long) transfer); // 转出
                redisTemplate.opsForValue().increment("Jack", (long) transfer); // 转入
                redisTemplate.exec(); // 提交事务
                System.out.println("双方转账成功");
            }else {
                System.out.println("Mike 账户余额不足，转账失败");
            }
        }catch (Exception e){
            System.out.println("转账过程中出现异常：" + e.getMessage());
            redisTemplate.discard(); // 回滚
        } finally {
            System.out.println("转账后双方余额：Mike：" + redisTemplate.opsForValue().get("Mike") + ", Jack: " + redisTemplate.opsForValue().get("Jack"));
            // 删除测试数据
            redisTemplate.delete(Arrays.asList("Mike", "Jack"));
        }
    }

}
