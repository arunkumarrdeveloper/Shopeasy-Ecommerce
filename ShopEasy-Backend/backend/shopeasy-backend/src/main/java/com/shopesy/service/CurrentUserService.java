package com.shopesy.service;

import com.shopesy.entity.AppUser;
import com.shopesy.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository users;
    public CurrentUserService(UserRepository users) { this.users = users; }

    public AppUser get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return users.findByEmail(auth.getName()).orElseThrow();
    }
}
