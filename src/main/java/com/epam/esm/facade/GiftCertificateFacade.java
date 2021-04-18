package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.GiftCertificate;

/**
 * Defines methods for facade layout for GiftCertificate class.
 * Methods supposed to wrap results in JsonResult class.
 */
public interface GiftCertificateFacade {

    JsonResult<GiftCertificate> getCertificate(int id);

    JsonResult<GiftCertificate> save(GiftCertificate certificate);

    JsonResult<GiftCertificate> delete(int id);

    /**
     * Searches GiftCertificate by nam and tag.
     *
     * @param name    - name or part name of GiftCertificate name.
     * @param tagName -  name or part name of Tag name.
     * @return - found GiftCertificates wrapped in JsonResult.
     */
    JsonResult<GiftCertificate> search(String name, String tagName);

    JsonResult<GiftCertificate> getAllCertificates();
}

