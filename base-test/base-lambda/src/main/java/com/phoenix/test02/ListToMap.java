package com.phoenix.test02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author phoenix
 * @Date 1/27/23 18:06
 * @Version 1.0
 */
public class ListToMap {

    /**
     * List<Object> 转 Map<String, String>
     */
    public static void main01(String[] args) {
        //声明一个List集合
        List<Student> list = new ArrayList<>();
        list.add(new Student("1001", "小A"));
        list.add(new Student("1001", "小B"));//学号重复（下面特殊处理）
        list.add(new Student("1002", "小C"));
        list.add(new Student("1003", "小D"));
        //将list转map （map的键重复不会报错，下面已经处理）

        Map<String, String> map =
                list.stream().collect(Collectors.toMap(
                        Student::getNo,
                        Student::getName,
                        (key1, key2) -> key2
                ));
        System.out.println(map);

        //Exception in thread "main" java.lang.IllegalStateException: Duplicate key 小A
        Map<String, String> map2 =
                list.stream().collect(Collectors.toMap(
                        Student::getNo,
                        Student::getName
                ));
        System.out.println(map2);
    }

    /**
     * List<Object> 转 Map<String, Object> (返回对象本身)
     */
    public static void main02(String[] args) {
        //声明一个List集合
        List<Student> list = new ArrayList<>();
        list.add(new Student("1001", "小A"));
        list.add(new Student("1001", "小B"));
        list.add(new Student("1002", "小C"));
        list.add(new Student("1003", "小D"));
        //将list转map （map的键去重）
        Map<String, Student> map =
                list.stream().collect(Collectors.toMap(
                        Student::getNo,
                        stu -> stu,
                        (key1, key2) -> key2
                ));
        System.out.println(map);

        //将list转map （map的键去重）
        Map<String, Student> map2 =
                list.stream().collect(Collectors.toMap(
                        Student::getNo,
                        Function.identity(),
                        (key1, key2) -> key2
                ));
        System.out.println(map2);
    }

    /**
     * List<Object2> 转 Map<String, Object1>
     */
    public static void main03(String[] args) {
        //声明一个List集合
        List<Student> list = new ArrayList<>();
        list.add(new Student("1001", "小A"));
        list.add(new Student("1001", "小B"));
        list.add(new Student("1002", "小C"));
        list.add(new Student("1003", "小D"));
        //将list转map （map的键去重）
        Map<String, Teacher> map =
                list.stream().collect(Collectors.toMap(
                        Student::getNo,
                        stu -> {
                            Teacher teacher = new Teacher();
                            teacher.setNo(stu.getNo());
                            teacher.setName(stu.getName());
                            return teacher;
                        },
                        (key1, key2) -> key2
                ));
        System.out.println(map);
    }

    /**
     * List<Object> 转 Map<String, List<Object>>
     */
    public static void main4(String[] args) {
        //声明一个List集合
        List<Student> list = new ArrayList<>();
        list.add(new Student("1001", "小A"));
        list.add(new Student("1001", "小B"));
        list.add(new Student("1002", "小C"));
        list.add(new Student("1003", "小D"));
        //将list转map （以某个属性来分组，将分组后相同的map放在一起）
        Map<String, List<Student>> map =
                list.stream().collect(Collectors.groupingBy(Student::getNo));
        System.out.println(map);
    }


}
