package com.shopesy.service;

import com.shopesy.dto.*;
import com.shopesy.entity.AppUser;
import com.shopesy.entity.Role;
import com.shopesy.repository.UserRepository;
import com.shopesy.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository users, PasswordEncoder encoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.users = users; this.encoder = encoder;
        this.authenticationManager = authenticationManager; this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        AppUser user = new AppUser(request.name(), request.email(),
                encoder.encode(request.password()), Role.USER);
        users.save(user);
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        AppUser user = users.findByEmail(request.email()).orElseThrow();
        return response(user);
    }

    private AuthResponse response(AppUser user) {
        return new AuthResponse(jwtService.generateToken(user), user.getId(),
                user.getName(), user.getEmail(), user.getRole().name());
    }
}
