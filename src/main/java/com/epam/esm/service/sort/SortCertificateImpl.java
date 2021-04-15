package com.epam.esm.service.sort;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.GiftCertificate;

import java.util.Comparator;
import java.util.List;

public class SortCertificateImpl implements SortCertificate {
    private String sortByName;
    private String sortByDate;

    public SortCertificateImpl(String sortByName, String sortByDate) {
        this.sortByName = sortByName;
        this.sortByDate = sortByDate;
    }

    @Override
    public void sort(List<GiftCertificate> certificates) {
        Comparator<GiftCertificate> comparator = getComparator();
        certificates.sort(comparator);
    }

    private Comparator<GiftCertificate> getComparator() {
        Comparator<GiftCertificate> comparator = null;
        if (sortByName != null) {
            comparator = Comparator.comparing(GiftCertificate::getName);
            if (sortByName.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
        }

        if (sortByDate != null) {
            if (comparator == null) {
                comparator = Comparator.comparing(GiftCertificate::getCreateDate);
            } else {
                comparator = comparator.thenComparing(GiftCertificate::getCreateDate);
            }
            if (sortByDate.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
        }
        return comparator;
    }
}