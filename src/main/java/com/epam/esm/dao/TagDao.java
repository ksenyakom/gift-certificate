package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao extends Dao<Tag>{
    List<Tag> readAll() throws DaoException;

}
