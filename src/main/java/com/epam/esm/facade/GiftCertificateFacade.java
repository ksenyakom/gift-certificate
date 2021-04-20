package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.GiftCertificate;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Defines methods for facade layout for GiftCertificate class.
 * Methods supposed to wrap results in JsonResult class.
 */

public interface GiftCertificateFacade {
    @NotNull
    JsonResult<GiftCertificate> getCertificate(int id);

    @NotNull
    JsonResult<GiftCertificate> save(GiftCertificate certificate);

    @NotNull
    JsonResult<GiftCertificate> delete(int id);

    /**
     * Searches GiftCertificate by name and tag name.
     *
     * @param name    - name or part name of GiftCertificate name.
     * @param tagName -  name or part name of Tag name.
     * @return - found GiftCertificates wrapped in JsonResult.
     */
    @NotNull
    JsonResult<GiftCertificate> search(@Nullable String name, @Nullable String tagName);

    void sort(@Nullable String sortByName, @Nullable String sortByDate, @NotNull List<GiftCertificate> certificates);

    @NotNull
    JsonResult<GiftCertificate> getAllCertificates();
}

