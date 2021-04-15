package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.GiftCertificate;

public interface GiftCertificateFacade {

    JsonResult<GiftCertificate> getCertificate(int id);

    JsonResult<GiftCertificate> save(GiftCertificate certificate);

    JsonResult<GiftCertificate> delete(int id);

    JsonResult<GiftCertificate> search(String name, String tagName);
}

