package com.phoenix;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Author phoenix
 * @Date 2022/11/24 16:39
 * @Version 1.0.0
 */
public class DoubleColonTest {


    public static void printValur(String str) {
        System.out.println("print value : " + str);
    }

    public static void main(String[] args) {
        List<String> al = Arrays.asList("a", "b", "c", "d");
        for (String a : al) {
            DoubleColonTest.printValur(a);
        }
        //下面的for each循环和上面的循环是等价的
        al.forEach(x -> {
            DoubleColonTest.printValur(x);
        });
        al.forEach(x -> DoubleColonTest.printValur(x));
        System.out.println("---------------------");
        al.forEach(DoubleColonTest::printValur);
        //下面的方法和上面等价的
        Consumer<String> methodParam = DoubleColonTest::printValur; //方法参数
        al.forEach(x -> methodParam.accept(x));//方法执行accept
    }

}
