package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.AccountRole;
import com.javagda25.securitytemplate.repository.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountRoleService {
    @Value("${account.default.roles:USER}")
    private String[] defaultRoles;

    private AccountRoleRepository accountRoleRepository;

    @Autowired
    public AccountRoleService(AccountRoleRepository accountRoleRepository) {
        this.accountRoleRepository = accountRoleRepository;
    }

    public Set<AccountRole> getDefaultRoles() {
        Set<AccountRole> accountRoles = new HashSet<>();

        for (String role : defaultRoles) {
            Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByName(role);
            accountRoleOptional.ifPresent(accountRoles::add);
        }

        return accountRoles;
    }

    public List<AccountRole> getAll() {
        return accountRoleRepository.findAll();
    }
}
