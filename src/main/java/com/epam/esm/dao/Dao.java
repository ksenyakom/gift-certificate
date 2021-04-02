package com.epam.esm.dao;


import com.epam.esm.model.Entity;

public interface Dao<Type extends Entity> {
	Integer create(Type entity) ;

	Type read(Integer id) ;

	void update(Type entity);

	void delete(Integer id) ;
}
