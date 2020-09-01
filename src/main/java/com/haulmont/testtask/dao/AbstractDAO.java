package com.haulmont.testtask.dao;

import java.util.List;

public interface AbstractDAO<T> {
    void add(T entity);
    void delete(Long id);
    void update(T entity);
    List<T> getAll();
}
