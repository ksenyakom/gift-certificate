package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.SearchParams;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.sort.SortGiftCertificate;
import com.epam.esm.service.sort.SortGiftCertificateImpl;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.SearchValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    @Autowired
    private GiftCertificateService giftCertificateService;
    @Autowired
    private GiftCertificateFacade giftCertificateFacade;

    @Autowired
    ApplicationContext context;

    @GetMapping()
    public JsonResult<GiftCertificate> index() {
        return  giftCertificateFacade.getAllCertificates();
    }

    @GetMapping("/{id}")
    public JsonResult<GiftCertificate> show(@PathVariable("id") int id) {
        return giftCertificateFacade.getCertificate(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<GiftCertificate> create(@RequestBody GiftCertificate certificate, BindingResult result) {
        GiftCertificateValidator validator = new GiftCertificateValidator();
        validator.validate(certificate, result);
        if (!result.hasErrors()) {
            return giftCertificateFacade.save(certificate);
        } else {
            throw new ServiceException(message(result), "20");
        }

    }

    @PatchMapping("/{id}")
    public JsonResult<GiftCertificate> update(@RequestBody GiftCertificate certificate, BindingResult result, @PathVariable("id") int id) {
        certificate.setId(id);
        GiftCertificateValidator validator = new GiftCertificateValidator();
        validator.validate(certificate, result);
        if (!result.hasErrors()) {
            return giftCertificateFacade.getCertificate(certificate.getId());
        } else {
            throw new ServiceException(message(result), "20");
        }
    }

    @DeleteMapping("/{id}")
    public JsonResult<GiftCertificate> delete(@PathVariable("id") int id) {
        return giftCertificateFacade.delete(id);
    }

    @GetMapping("/search")
    public JsonResult<GiftCertificate> search(@ModelAttribute SearchParams searchParams,
                                              BindingResult result) {
        SearchValidator searchValidator = new SearchValidator();
        searchValidator.validate(searchParams, result);

        if (!result.hasErrors()) {
            JsonResult<GiftCertificate> jsonResult = giftCertificateFacade.search(
                    searchParams.getName(), searchParams.getTagName());

            if (jsonResult.getResult() != null) {
                String sortByName = searchParams.getSortByName();
                String sortByDate = searchParams.getSortByDate();
                if ( sortByName != null || sortByDate != null) {
                    SortGiftCertificate sortCertificate = new SortGiftCertificateImpl(sortByName, sortByDate);
                    sortCertificate.sort(jsonResult.getResult());
                }
            }
            return jsonResult;
        } else {
            throw new ServiceException(message(result), "24");
        }
    }


    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors().stream()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getCode())
                        .append(";"));
        return sb.toString();
    }

}
