package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {

    //    private static List<GiftCertificate> repository = new ArrayList<>();
//    private static int i = 0;
//
//    {
//        GiftCertificate gift = new GiftCertificate();
//        gift.setId(++i);
//        gift.setName("Beauty");
//        repository.add(gift);
//        gift = new GiftCertificate();
//        gift.setId(++i);
//        gift.setName("Haircut");
//        repository.add(gift);
//        gift = new GiftCertificate();
//        gift.setId(++i);
//        gift.setName("Manicure");
//        repository.add(gift);
//        gift = new GiftCertificate();
//        gift.setId(++i);
//        gift.setName("Spa");
//        repository.add(gift);
//    }
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public GiftCertificate findById(Integer id) throws ServiceException {
        return giftCertificateDao.read(id);

//        return repository.stream()
//                .filter(gift -> gift.getId().equals(id))
//                .findAny().orElse(null);
    }

    @Override
    public List<GiftCertificate> findAll() throws ServiceException {
        return giftCertificateDao.readAll();
    }

    @Override
    public void save(GiftCertificate entity) throws ServiceException {
        if (entity.getId() == null) {
            giftCertificateDao.create(entity);
//            entity.setId(++i);
//            repository.add(entity);
        } else {
            giftCertificateDao.update(entity);
//            GiftCertificate certificate = findById(entity.getId());
//            certificate.setName(entity.getName());
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        giftCertificateDao.delete(id);
      //  repository.removeIf(certificate -> certificate.getId().equals(id));
    }
}
