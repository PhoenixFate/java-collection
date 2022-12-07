package com.phoenix;

/**
 * @Author phoenix
 * @Date 2022/12/7 17:05
 * @Version 1.0.0
 */
public enum Prizes {
    one("one", 1, "一等奖"),
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

    public static Prizes codeOf(int code) {
        for (Prizes prizes : values()) {
            if (prizes.getValue() == code) {
                return prizes;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }

    public static void main(String[] args) {
        System.out.println(Prizes.codeOf(1).getFiled());
    }

}

