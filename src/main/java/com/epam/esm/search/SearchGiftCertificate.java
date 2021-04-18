package com.epam.esm.search;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;

import java.util.List;

/**
 * Interface for searching of GiftCertificate.
 */
public interface SearchGiftCertificate {
        List<GiftCertificate> search(GiftCertificateService service) ;
}
