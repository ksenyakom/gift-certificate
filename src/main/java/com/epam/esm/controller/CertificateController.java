package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.facade.impl.GiftCertificateFacadeImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.sort.SortCertificate;
import com.epam.esm.service.sort.SortCertificateImpl;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.xml.validation.Validator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

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
    public List<GiftCertificate> index() {
        return giftCertificateService.findAll();
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
            return giftCertificateFacade.save(certificate);
        } else {
            throw new ServiceException(message(result), "20");
        }
    }

    @DeleteMapping("/{id}")
    public JsonResult<GiftCertificate> delete(@PathVariable("id") int id) {
        return giftCertificateFacade.delete(id);
    }

    @GetMapping("/search")
    public JsonResult<GiftCertificate> search(@RequestParam(value = "name", required = false) String name,
                                              @RequestParam(value = "tagName", required = false) String tagName,
                                              @RequestParam(value = "sortByDate", required = false) String sortByDate,
                                              @RequestParam(value = "sortByName", required = false) String sortByName) {
        //TODO validator??
        JsonResult<GiftCertificate> jsonResult= giftCertificateFacade.search(name, tagName);
        if (sortByName != null || sortByDate != null) {
            SortCertificate sortCertificate = new SortCertificateImpl(sortByName, sortByDate);
            sortCertificate.sort(jsonResult.getResult());
        }
        return jsonResult;
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
