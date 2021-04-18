package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.search.SearchGiftCertificateImpl;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GiftCertificateFacadeImpl implements GiftCertificateFacade {
    @Autowired
    private GiftCertificateService giftCertificateService;

    public GiftCertificateFacadeImpl(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    public JsonResult<GiftCertificate> getCertificate(int id) {
        GiftCertificate certificate = giftCertificateService.findById(id);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(Collections.singletonList(certificate))
                .build();
    }


    @Override
    public JsonResult<GiftCertificate> save(GiftCertificate certificate) {
        giftCertificateService.save(certificate);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(Collections.singletonList(certificate))
                .build();
    }

    @Override
    public JsonResult<GiftCertificate> delete(int id) {
        giftCertificateService.delete(id);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .build();
    }

    @Override
    public JsonResult<GiftCertificate> search(String name, String tagName) {
        SearchGiftCertificateImpl searchCertificate = new SearchGiftCertificateImpl(name,tagName);
        List<GiftCertificate> certificateList = searchCertificate.search(giftCertificateService);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(certificateList)
                .build();
    }

    @Override
    public JsonResult<GiftCertificate> getAllCertificates() {
        List<GiftCertificate> certificates = giftCertificateService.findAll();
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(certificates)
                .build();
    }
}
