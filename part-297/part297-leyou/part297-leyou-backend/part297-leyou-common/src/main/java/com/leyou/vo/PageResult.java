package com.leyou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {

    private Long total;//总条数
    private Integer totalPage;//总页数
    private List<T> data;//当前页的数据

}
