package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;

import java.util.List;


public interface GiftCertificateService {
    GiftCertificate findById(Integer id) throws ServiceException;

    List<GiftCertificate> findAll() throws ServiceException;

    void save(GiftCertificate entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
