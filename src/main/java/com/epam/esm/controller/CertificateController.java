package com.epam.esm.controller;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public List<GiftCertificate> index(Model model) {
      //  return "certificates/index";
        return giftCertificateService.findAll();

    }

    @GetMapping("/{id}")
    public GiftCertificate show(@PathVariable("id") int id, Model model) {

        return giftCertificateService.findById(id);
    }

    @GetMapping("/new")
    public String newCertificate(Model model) {
        model.addAttribute("certificate", new GiftCertificate());

        return "certificates/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("certificate") GiftCertificate certificate) {
            giftCertificateService.save(certificate);

        return "redirect:/certificates";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
            model.addAttribute("certificate", giftCertificateService.findById(id));
        return "certificates/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("certificate") GiftCertificate certificate, @PathVariable("id") int id) {
            giftCertificateService.save(certificate);
        return "redirect:/certificates";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
            giftCertificateService.delete(id);
        return "redirect:/certificates";
    }

//    @ExceptionHandler(ServiceException.class)
//    public ServiceException someError(ServiceException e) {
//
//        return
//
}
