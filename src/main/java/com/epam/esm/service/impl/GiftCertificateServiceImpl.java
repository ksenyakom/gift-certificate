package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public GiftCertificate findById(Integer id) {
        return giftCertificateDao.read(id);

    }

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateDao.readAll();
    }

    @Override
    public void save(GiftCertificate entity) {
        if (entity.getId() == null) {
            giftCertificateDao.create(entity);
        } else {
            giftCertificateDao.update(entity);
        }
    }

    @Override
    public void delete(Integer id) {
        giftCertificateDao.delete(id);
    }
}
