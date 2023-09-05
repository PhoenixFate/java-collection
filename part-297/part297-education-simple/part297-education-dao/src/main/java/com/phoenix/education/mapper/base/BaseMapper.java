package com.phoenix.education.mapper.base;

import java.io.Serializable;

public interface BaseMapper<T> {

    T findById(Serializable id);

    int update(T t);

    int insert(T t);

    int deleteById(Serializable id);

}
