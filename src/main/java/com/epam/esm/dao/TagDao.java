package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao {

    Integer create(Tag tag) throws DaoException;

    Tag read(Integer id) throws DaoException;

    void read(Tag tag) throws DaoException;

    void update(Tag tag) throws DaoException;

    void delete(Integer id) throws DaoException;

    List<Tag> readAll() throws DaoException;

    List<GiftCertificate> readCertificateByTag(Integer id) throws DaoException;


}
