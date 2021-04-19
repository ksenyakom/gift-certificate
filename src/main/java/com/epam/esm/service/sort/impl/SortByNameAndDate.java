package com.epam.esm.service.sort.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.sort.SortGiftCertificateService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

public class SortByNameAndDate implements SortGiftCertificateService {
    /**
     * if value != null, then sorting by name performed.
     * Value must be "asc" or "desc" to specify sort order.
     */
    private String sortByName;
    /**
     * if value != null, then sorting by date created performed.
     * Value must be "asc" or "desc" to specify sort order.
     */
    private String sortByDate;

    public SortByNameAndDate(String sortByName, String sortByDate) {
        this.sortByName = sortByName;
        this.sortByDate = sortByDate;
    }

    @Override
    public void sort(List<GiftCertificate> certificates) {
        if (sortByDate != null || sortByDate != null) {
            Comparator<GiftCertificate> comparator = getComparator();
            certificates.sort(comparator);
        }
    }

    private Comparator<GiftCertificate> getComparator() {
        Comparator<GiftCertificate> comparator = null;
        if (sortByName != null) {
            comparator = addSortByNameComparison();
        }
        if (sortByDate != null) {
            comparator = addSortByDateComparison(comparator);
        }

        return comparator;
    }

    private Comparator<GiftCertificate> addSortByNameComparison() {
        Comparator<GiftCertificate> comparator = Comparator.comparing(GiftCertificate::getName);
        if (sortByName.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private Comparator<GiftCertificate> addSortByDateComparison(Comparator<GiftCertificate> comparator) {
        if (comparator == null) {
            comparator = Comparator.comparing(GiftCertificate::getCreateDate);
            if (sortByDate.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
        } else {
            if (sortByDate.equalsIgnoreCase("desc")) {
                comparator = comparator.thenComparing(Comparator.comparing(GiftCertificate::getCreateDate).reversed());
            } else {
                comparator = comparator.thenComparing(GiftCertificate::getCreateDate);
            }
        }

        return comparator;
    }
}