package com.epam.esm.service;

import com.epam.esm.model.Entity;

import java.util.List;

public interface Service<T extends Entity> {
    T findById(Integer id) ;

    List<T> findAll() ;

    void save(T entity) ;

    void delete(Integer id) ;

}
