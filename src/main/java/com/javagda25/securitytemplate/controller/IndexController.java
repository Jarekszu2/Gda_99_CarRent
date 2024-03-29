package com.javagda25.securitytemplate.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
@AllArgsConstructor
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/tylkodlakozakow")
    public String kozaki(){
        return "kozaki";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login-form";
    }

    @GetMapping("/faq")
    public String faq(){ return "about-us";}

    @GetMapping("/contact-form")
    public String contactFrom(){return "contact-form";}

    @GetMapping("/rental-conditions")
    public String rentalConditions(){return "rental-conditions";}
}
