package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificate findById(Integer id) throws ServiceException {
        try {
            GiftCertificate certificate = giftCertificateDao.read(id);
            readTagName(certificate);
            return certificate;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws ServiceException {
        try {
            List<GiftCertificate> certificates = giftCertificateDao.readAll();
            readTagName(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public void save(GiftCertificate certificate) throws ServiceException {
        try {
            checkForNewTags(certificate.getTags());
            if (certificate.getId() == null) {
                certificate.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
                certificate.setId(giftCertificateDao.create(certificate));
            } else {
                certificate.setLastUpdateDate(LocalDateTime.now(ZoneOffset.UTC));
                giftCertificateDao.update(certificate);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    private void checkForNewTags(List<Tag> tags) throws DaoException {
        for (Tag tag : tags) {
            if (tag.getId() == null) {
                tag.setId(tagDao.create(tag));
            }
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

    private void readTagName(GiftCertificate certificate) throws DaoException {
        for (Tag tag : certificate.getTags()) {
            tagDao.read(tag);
        }
    }

    private void readTagName(List<GiftCertificate> certificates) throws DaoException {
        for (GiftCertificate certificate : certificates) {
            readTagName(certificate);
        }
    }
}
