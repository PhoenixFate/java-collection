package com.phoenix.blog.article.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Author phoenix
 * @Date 10/15/22 16:55
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class ArticleCategoryItemDTO {

    private String name;

    private BigDecimal value;
}
