package com.epam.esm.service;

import com.epam.esm.model.Entity;

import java.util.List;

public interface Service<T extends Entity> {
    T findById(Integer id) throws ServiceException;

    List<T> findAll() throws ServiceException;

    void save(T entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;

}
