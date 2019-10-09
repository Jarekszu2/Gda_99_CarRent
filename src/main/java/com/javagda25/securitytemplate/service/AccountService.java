package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRoleService accountRoleService;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountRoleService accountRoleService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRoleService = accountRoleService;
    }

    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }

        // szyfrowanie hasła
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getDefaultRoles());

        // zapis do bazy
        accountRepository.save(account);

        return true;
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public void toggleLock(Long accountId) {
        if(accountRepository.existsById(accountId)){
            Account account = accountRepository.getOne(accountId);
            account.setLocked(!account.isLocked());

            accountRepository.save(account);
        }
    }
}
