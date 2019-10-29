package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping(path = "/account/")
public class RentalController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/rentals")
    public String addRent(Model model, Principal principal) {
        if (principal == null) {
            // nie jest zalogowany
            // todo: zrob cos
        } else {
            Account account = accountService.findByUsername(principal.getName());
            model.addAttribute("rentals", account.getBookingsClient());
// todo: wyświetl
        }
    }
}
