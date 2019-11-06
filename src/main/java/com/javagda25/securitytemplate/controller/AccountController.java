package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;


@Controller
@RequestMapping(path = "/user/")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String registrationForm(Model model, Account account) {
        model.addAttribute("newAccount", account);

        return "registration-form";
    }

    @PostMapping("/register")
    public String register(@Valid Account account,
                           BindingResult result,
                           String passwordConfirm,
                           Model model) {

        if (result.hasErrors()) {
            return registrationError(model, account, result.getFieldError().getDefaultMessage());
        }

        // todo: tworzenie konta
        if (!account.getPassword().equals(passwordConfirm)) {
            return registrationError(model, account, "Passwords do not match.");
        }

        if (!accountService.register(account)) {
            return registrationError(model, account, "User with given username already exists.");
        }

        return "redirect:/login";
    }

    private String registrationError(Model model, Account account, String message) {
        model.addAttribute("newAccount", account);
        model.addAttribute("errorMessage", message);

        return "registration-form";
    }

    @GetMapping("/client")
    public String client(Model model,
                         Principal principal) {
        if (principal == null) {
            // nie jest zalogowany
            return "login-form";
        } else {
            Account account = accountService.findByUsername(principal.getName());
            model.addAttribute("account", account);
            return "account-client";
        }
    }

//    @PostMapping(path = "/client")
    @PostMapping("/client")
    public String postClient() {
//        accountService.save(client);
        return "redirect:/user/edit";
    }


    @GetMapping("/edit")
    public String edit(Model model,
                       Principal principal) {
        Account account = accountService.findByUsername(principal.getName());
        model.addAttribute("account", account);
        return "account-edit";
    }

    @PostMapping("/edit")
    public String postEdit(Principal principal,
                           Account client,
                           String newPassword) {
        accountService.update(principal.getName(), client, newPassword);
        return "redirect:/car/list_cars";
    }

    private String showRegisteredName(Principal principal) {
        return principal.getName();
    }

}
