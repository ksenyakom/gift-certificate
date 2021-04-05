package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping()
    public  @ResponseBody List<GiftCertificate> index() {
       // throw new ServiceException("Some error",1234);
        return giftCertificateService.findAll();

    }

    @GetMapping("/{id}")
    public GiftCertificate show(@PathVariable("id") int id) {
        return giftCertificateService.findById(id);
    }

    @PostMapping()
    public @ResponseBody List<GiftCertificate> create(@RequestBody GiftCertificate giftCertificate){
            giftCertificateService.save(giftCertificate);
        return giftCertificateService.findAll();
    }

    @PatchMapping("/{id}")
    public @ResponseBody GiftCertificate update(@RequestBody GiftCertificate certificate, @PathVariable("id") int id) {
        certificate.setId(id);
        giftCertificateService.save(certificate);
        return giftCertificateService.findById(id);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody List<GiftCertificate> delete(@PathVariable("id") int id) {
            giftCertificateService.delete(id);
        return giftCertificateService.findAll();
    }

    @ExceptionHandler(ServiceException.class)
    public @ResponseBody Map<String,String> someError(ServiceException e) {
        Map<String,String> error = new HashMap<>();
        error.put("errorMessage", e.getMessage());
        error.put("errorCode", "" + e.getErrorCode());
        return error;
    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public @ResponseBody Map<String,String> someError(ServiceException e) {
//        Map<String,String> error = new HashMap<>();
//        error.put("errorMessage", e.getMessage());
//        error.put("errorCode", "" + e.getErrorCode());
//        return error;
//    }

}
