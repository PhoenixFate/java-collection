package com.phoenix.blog.article.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author phoenix
 * @Date 10/15/22 16:55
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class ArticleCategoryTotalDTO {

    private List<ArticleCategoryItemDTO> nameAndValueList;

    private List<String> nameList;
}