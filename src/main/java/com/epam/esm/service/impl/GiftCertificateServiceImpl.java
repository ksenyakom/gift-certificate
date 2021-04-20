package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;

    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    @Nullable
    public GiftCertificate findById(Integer id) throws ServiceException {
        try {
            GiftCertificate certificate = giftCertificateDao.read(id);
            if (certificate != null) {
                readTagName(certificate.getTags());
            }
            return certificate;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NotNull
    public List<GiftCertificate> findAll() throws ServiceException {
        try {
            List<GiftCertificate> certificates = giftCertificateDao.readAll();
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void save(@NotNull GiftCertificate certificate) throws ServiceException {
        try {
            checkForNewTags(certificate.getTags());
            if (certificate.getId() == null) {
                certificate.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
                certificate.setIsActive(true);
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
        if (tags != null) {
            for (Tag tag : tags) {
                if (tag.getId() == null) {
                    tag.setId(tagDao.create(tag));
                }
            }
        }
    }

    @Transactional
    @Override
    public void delete(@NotNull Integer id) throws ServiceException {
        try {
            giftCertificateDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NotNull
    public List<GiftCertificate> findByTagName(@NotNull String tagName) {
        try {
            List<Tag> tags = tagDao.readByName(tagName);
            List<GiftCertificate> certificates = giftCertificateDao.readByTags(tags);
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NotNull
    public List<GiftCertificate> findByName(@NotNull String name) {
        try {
            List<GiftCertificate> certificates = giftCertificateDao.readByName(name);
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NotNull
    public List<GiftCertificate> findByNameAndTagName(@NotNull String name, @NotNull String tagName) {
        try {
            List<Tag> tags = tagDao.readByName(tagName);
            List<GiftCertificate> certificates = giftCertificateDao.readByNameAndTagName(name, tags);
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    private void readTagName(List<Tag> tags) throws DaoException {
        if (tags != null) {
            for (Tag tag : tags) {
                tagDao.read(tag);
            }
        }
    }

    private void readTagNames(List<GiftCertificate> certificates) throws DaoException {
        List<Tag> tags = certificates.stream()
                .filter(certificate -> certificate.getTags() != null)
                .flatMap(certificate -> certificate.getTags().stream())
                .distinct()
                .collect(Collectors.toList());
        readTagName(tags);
    }
}
