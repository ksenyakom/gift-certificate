package com.epam.esm.service;

import com.epam.esm.dao.DaoException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.GiftCertificate;

import java.util.List;


public interface GiftCertificateService {
    GiftCertificate findById(Integer id) throws ServiceException;

    List<GiftCertificate> findAll() throws ServiceException;

    void save(GiftCertificate entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    List<GiftCertificate> findByTagName(String tagName) ;

    List<GiftCertificate> findByName(String name);

    List<GiftCertificate> findByNameAndTagName(String name, String tagName);
}
