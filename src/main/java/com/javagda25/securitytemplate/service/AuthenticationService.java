package com.javagda25.securitytemplate.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if(s.equals("admin")) {
            return User.builder()
                    .username("admin")
                    .password("admin")
                    .build();
        }
        throw new UsernameNotFoundException("Username not found.");
    }
}
