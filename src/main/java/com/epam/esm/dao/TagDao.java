package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao extends Dao<Tag>{
    void read(Tag tag) throws DaoException;

    List<Tag> readAll() throws DaoException;
    List<GiftCertificate> readCertificateByTag(Integer id) throws DaoException;

}
