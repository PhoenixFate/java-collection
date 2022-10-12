package com.phoenix.blog.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章状态枚举类
 *
 * @Author phoenix
 * @Date 10/12/22 23:27
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {

    DELETE(0, "已删除"),
    WAIT(1, "待审核"),
    SUCCESS(2, "审核通过"),
    FAIL(3, "审核不通过");

    private final Integer code;
    private final String desc;
}
