package com.phoenix;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

/**
 * @Author phoenix
 * @Date 2022/12/7 17:05
 * @Version 1.0.0
 */
public enum Prizes {
    ONE("one", 1, "一等奖"),
    two("two", 2, "二等奖"),
    three("three", 3, "三等奖"),
    four("four", 4, "四等奖"),
    five("five", 5, "五等奖"),
    six("six", 6, "六等奖");

    private final String key;
    private final Integer value;
    private final String filed;

    private Prizes(String key, Integer value, String filed) {
        this.key = key;
        this.value = value;
        this.filed = filed;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    public String getFiled() {
        return filed;
    }

    public static Prizes keyOf(String key) {
        for (Prizes prizes : values()) {
            if (prizes.key.equals(key)) {
                return prizes;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }


    public static Prizes codeOf(int code) {
        for (Prizes prizes : values()) {
            if (prizes.getValue() == code) {
                return prizes;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }


    /**
     * @param key
     * @return
     * @Description: 根据key获取枚举对象
     * @version v1.0
     * @author wu
     * @date 2022/8/1 20:28
     */
    public static Prizes get(String key) {
        Iterator<Prizes> iterator = Arrays.stream(Prizes.values()).iterator();
        while (iterator.hasNext()) {
            Prizes next = iterator.next();
            if (next.key.equals(key)) {
                return next;
            }
        }
        return null;
    }

    /**
     * @param
     * @return
     * @Description: 根据code获取枚举对象 --- Java8方式
     * @version v1.0
     * @author wu
     * @date 2022/8/1 20:30
     */
    public static Prizes getByJava8(String code) {
        Optional<Prizes> first = Arrays.stream(Prizes.values()).filter(e -> e.key.equals(code)).findFirst();
        // Prizes expEnum0 = first.get();
        Prizes expEnum = first.orElse(null);
        return expEnum;
    }


    public static void main(String[] args) {
        System.out.println(Prizes.codeOf(1).getFiled());
        System.out.println(Prizes.keyOf("two").getFiled());
        System.out.println(Prizes.valueOf("ONE"));

        Prizes[] values = Prizes.values();
        for (Prizes e : values) {
            System.out.println(e.ordinal());
        }

    }

}

