package com.epam.esm.service.sort;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface SortCertificate {
    void sort(List<GiftCertificate> certificates);
}
