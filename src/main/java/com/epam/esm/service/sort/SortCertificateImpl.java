package com.epam.esm.service.sort;

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