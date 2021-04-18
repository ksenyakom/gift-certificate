package com.epam.esm.sort;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

/**
 * Interface for sorting of GiftCertificate.
 */
public interface SortGiftCertificate {
    void sort(List<GiftCertificate> certificates);
}
