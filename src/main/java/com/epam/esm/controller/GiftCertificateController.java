package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.SearchParams;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.sort.SortGiftCertificateService;
import com.epam.esm.service.sort.impl.SortByNameAndDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller class for GiftCertificate
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    @Autowired
    private GiftCertificateFacade giftCertificateFacade;

    @Autowired
    @Qualifier("giftCertificateValidator")
    private Validator validator;

    @Autowired
    @Qualifier("searchGiftCertificateValidator")
    private Validator searchValidator;


    @GetMapping()
    public JsonResult<GiftCertificate> index() {

        return giftCertificateFacade.getAllCertificates();
    }

    @GetMapping("/{id}")
    public JsonResult<GiftCertificate> show(@PathVariable("id") int id) {

        return giftCertificateFacade.getCertificate(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<GiftCertificate> create(@RequestBody GiftCertificate certificate, BindingResult result) {
        validator.validate(certificate, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return giftCertificateFacade.save(certificate);
    }

    @PatchMapping("/{id}")
    public JsonResult<GiftCertificate> update(@RequestBody GiftCertificate certificate, BindingResult result,
            @PathVariable("id") int id) {
        certificate.setId(id);
        validator.validate(certificate, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return giftCertificateFacade.save(certificate);
    }

    @DeleteMapping("/{id}")
    public JsonResult<GiftCertificate> delete(@PathVariable("id") int id) {

        return giftCertificateFacade.delete(id);
    }

    @GetMapping("/search")
    public JsonResult<GiftCertificate> search(@ModelAttribute SearchParams searchParams, BindingResult result) {
        searchValidator.validate(searchParams, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "24");
        }

        JsonResult<GiftCertificate> jsonResult = giftCertificateFacade.search(searchParams.getName(), searchParams.getTagName());
        Optional.ofNullable(jsonResult.getResult()).ifPresent(certificates -> {
            SortGiftCertificateService sortCertificate = new SortByNameAndDate(searchParams.getSortByName(), searchParams.getSortByDate());
            sortCertificate.sort(certificates);
        });

        return jsonResult;
    }


    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField()).append(": ")
                        .append(fieldError.getCode()).append(";"));

        return sb.toString();
    }

}