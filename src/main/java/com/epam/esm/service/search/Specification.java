package com.epam.esm.service.search;

import com.epam.esm.dao.DaoException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;

import java.util.List;

public interface Specification {
        List<GiftCertificate> search(GiftCertificateService service) ;
}
