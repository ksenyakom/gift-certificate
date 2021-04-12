package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.xml.validation.Validator;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping()
    public List<GiftCertificate> index() throws ServiceException {
        return giftCertificateService.findAll();
    }

    @GetMapping("/{id}")
    public GiftCertificate show(@PathVariable("id") int id) throws ServiceException {
        return giftCertificateService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate create(@RequestBody GiftCertificate certificate, BindingResult result) throws ServiceException {
        GiftCertificateValidator validator = new GiftCertificateValidator();
        validator.validate(certificate, result);
        if (result.hasErrors()) {
            throw new ServiceException(result.getAllErrors().toString(), "20");
        } else {
            giftCertificateService.save(certificate);
            return certificate;
        }

    }

    @PatchMapping("/{id}")
    public GiftCertificate update(@RequestBody GiftCertificate certificate, BindingResult result, @PathVariable("id") int id) throws ServiceException {
        certificate.setId(id);
        GiftCertificateValidator validator = new GiftCertificateValidator();
        validator.validate(certificate, result);
        if (result.hasErrors()) {
            throw new ServiceException(result.getAllErrors().toString(), "20");
        } else {
            giftCertificateService.save(certificate);
            return certificate;
        }
    }

    @DeleteMapping("/{id}")
    public List<GiftCertificate> delete(@PathVariable("id") int id) throws ServiceException {
        giftCertificateService.delete(id);
        return giftCertificateService.findAll();
    }

}
