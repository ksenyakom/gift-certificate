package com.epam.esm.controller;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/certificates")
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping()
    public String index(Model model) {
        try {
            model.addAttribute("certificates", giftCertificateService.findAll());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "certificates/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("certificate", giftCertificateService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "certificates/show";
    }

    @GetMapping("/new")
    public String newCertificate(Model model) {
        model.addAttribute("certificate", new GiftCertificate());

        return "certificates/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("certificate") GiftCertificate certificate) {
        try {
            giftCertificateService.save(certificate);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/certificates";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("certificate", giftCertificateService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "certificates/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("certificate") GiftCertificate certificate, @PathVariable("id") int id) {
        try {
            giftCertificateService.save(certificate);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/certificates";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            giftCertificateService.delete(id);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/certificates";
    }
}
