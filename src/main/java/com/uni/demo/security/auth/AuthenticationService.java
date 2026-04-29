package com.uni.demo.security.auth;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.uni.demo.security.user.UserRepository;
import com.uni.demo.security.user.Role;
import com.uni.demo.security.user.User;
import com.uni.demo.security.config.JwtService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {

        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
//fixed
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        // ONLY enforce if JWT-based authentication exists
        if (currentAuth != null
                && currentAuth.isAuthenticated()
                && currentAuth.getPrincipal() instanceof UserDetails) {

            String tokenUserEmail = ((UserDetails) currentAuth.getPrincipal()).getUsername();

            if (!tokenUserEmail.equals(request.getEmail())) {
                throw new AccessDeniedException("Token does not match requested user");
            }
        }
//fixed
        // normal login
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}