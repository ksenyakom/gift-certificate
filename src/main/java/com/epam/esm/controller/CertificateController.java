package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public GiftCertificate create(@RequestBody GiftCertificate giftCertificate) throws ServiceException {
        giftCertificateService.save(giftCertificate);

        return giftCertificateService.findById(giftCertificate.getId());
    }

    @PatchMapping("/{id}")
    public GiftCertificate update(@RequestBody GiftCertificate certificate, @PathVariable("id") int id) throws ServiceException {
        certificate.setId(id);
        giftCertificateService.save(certificate);
        return giftCertificateService.findById(id);
    }

    @DeleteMapping("/{id}")
    public List<GiftCertificate> delete(@PathVariable("id") int id) throws ServiceException {
        giftCertificateService.delete(id);
        return giftCertificateService.findAll();
    }

}
