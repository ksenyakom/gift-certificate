package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagService {
    Tag findById(Integer id) throws ServiceException;

    List<Tag> findAll() throws ServiceException;

    void save(Tag entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
