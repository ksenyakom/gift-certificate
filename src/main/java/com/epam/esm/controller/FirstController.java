package com.epam.esm.controller;

import com.epam.esm.dao.GiftCertificateDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

    @GetMapping("/hello")
    public String helloPage (){

        return "first/hello";
    }

    @GetMapping("/bye")
    public String goodByePage(){
        return "first/goodbye";
    }
}
