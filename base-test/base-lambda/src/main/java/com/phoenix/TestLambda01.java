package com.phoenix;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * @Author phoenix
 * @Date 2022/11/25 9:55
 * @Version 1.0.0
 */
public class TestLambda01 {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        }).start();
        new Thread(() -> {
            System.out.println("hello2");
        }).start();


        ArrayList<String> list = new ArrayList<>();
        list.add("AAAAA");
        list.add("BBBBB");
        list.add("CCCCC");
        list.add("DDDDD");
        //形参的类型是确定的，可省略；只有一个形参，()可以省略；
        list.forEach(t -> System.out.print(t + "\t"));
        System.out.println("");
        //或者更简洁的方法引用：list.forEach(System.out::println);
        //Random random = new Random();
        //Stream<Integer> stream = Stream.generate(() -> random.nextInt(100));
        //stream.forEach(t -> System.out.println(t));

        Collator collator = Collator.getInstance();
        TreeSet<Student> treeSet = new TreeSet<>((s1, s2) -> collator.compare(s1.getName(), s2.getName()));
        treeSet.add(new Student(10, "张飞"));
        treeSet.add(new Student(3, "周瑜"));
        treeSet.add(new Student(1, "宋江"));
        treeSet.forEach(student -> System.out.println(student));



    }
}
