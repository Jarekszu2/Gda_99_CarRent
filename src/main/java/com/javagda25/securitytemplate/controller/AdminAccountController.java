package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.model.dto.AccountPasswordResetRequest;
import com.javagda25.securitytemplate.service.AccountRoleService;
import com.javagda25.securitytemplate.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/account/")
@PreAuthorize(value = "hasRole('ADMIN')")
public class AdminAccountController {

    private AccountService accountService;
    private AccountRoleService accountRoleService;

    @Autowired
    public AdminAccountController(AccountService accountService, AccountRoleService accountRoleService) {
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
    }

    @GetMapping("/list")
    public String getUserList(Model model) {
        model.addAttribute("accounts", accountService.getAll());
        return "account-list";
    }

    @GetMapping("/toggleLock")
    public String toggleLock(@RequestParam(name = "accountId") Long accountId) {
        accountService.toggleLock(accountId);

        return "redirect:/admin/account/listCars";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam(name = "accountId") Long accountId) {
        accountService.remove(accountId);

        return "redirect:/admin/account/listCars";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(Model model, @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);

        if (accountOptional.isPresent()) {
            model.addAttribute("account", accountOptional.get());
            return "account-passwordreset";
        }
        return "redirect:/admin/account/listCars";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(AccountPasswordResetRequest request) {
        accountService.resetPassword(request);

        return "redirect:/admin/account/listCars";
    }

    @GetMapping("/editRoles")
    public String editRoles(Model model, @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (accountOptional.isPresent()) {
            model.addAttribute("roles", accountRoleService.getAll());
            model.addAttribute("account", accountOptional.get());

            return "account-roles";
        }
        return "redirect:/admin/account/listCars";
    }

    @PostMapping("/editRoles")
    public String editRoles(Long accountId, HttpServletRequest request) {
        accountService.editRoles(accountId, request);

        return "redirect:/admin/account/listCars";
    }
}
