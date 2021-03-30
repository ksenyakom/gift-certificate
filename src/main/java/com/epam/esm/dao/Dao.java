package com.epam.esm.dao;


import com.epam.esm.entity.Entity;
import com.epam.esm.exception.PersistentException;

import java.util.List;

public interface Dao<Type extends Entity> {
	Integer create(Type entity) throws DaoException;

	Type read(Integer id) throws DaoException;

	void update(Type entity) throws DaoException;

	void delete(Integer id) throws DaoException;
}
