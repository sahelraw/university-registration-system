package com.uni.demo.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AppUserRepository appUserRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        AppUser appUser = new AppUser(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.role() == null ? AppUserRole.USER : request.role()
        );

        appUserRepository.save(appUser);
        String jwtToken = jwtService.generateToken(buildUserDetails(appUser));
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        AppUser appUser = appUserRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        String jwtToken = jwtService.generateToken(buildUserDetails(appUser));
        return new AuthResponse(jwtToken);
    }

    private org.springframework.security.core.userdetails.UserDetails buildUserDetails(AppUser user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
