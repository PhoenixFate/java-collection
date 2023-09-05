package com.phoenix.education.service.base;


import java.io.Serializable;

public interface IBaseService<T> {

    T findById(Serializable id);

    int update(T t);

    int insert(T t);

    int deleteById(Serializable id);
}
