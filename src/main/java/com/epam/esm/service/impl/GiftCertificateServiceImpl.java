package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public GiftCertificate findById(Integer id) throws ServiceException {
        try {
            return giftCertificateDao.read(id);
        }  catch (DaoException e) {
        throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
    }

    }

    @Override
    public List<GiftCertificate> findAll() throws ServiceException {
        try {
            return giftCertificateDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public void save(GiftCertificate entity) throws ServiceException {
        try {
            if (entity.getId() == null) {
                entity.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
                entity.setId(giftCertificateDao.create(entity));
            } else {
                entity.setLastUpdateDate(LocalDateTime.now(ZoneOffset.UTC));
                giftCertificateDao.update(entity);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            giftCertificateDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }
}
