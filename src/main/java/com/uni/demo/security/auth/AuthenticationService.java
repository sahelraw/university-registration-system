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


@Service
@RequiredArgsConstructor

public class AuthenticationService {

    private final UserRepository repository; //to inject the UserRepository class that we will implement later in ...userRepository.java.. to handle the database operations for the user entity such as saving a new user and finding a user by email and then we will implement the logic for these operations in the authentication service that we will implement later to allow the user to register a new account and log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
    private final PasswordEncoder passwordEncoder; //to inject the PasswordEncoder class that we will implement later in ...config.java.. to handle the password encoding and decoding for the user entity such as encoding the password before saving it to the database and decoding the password during authentication and then we will implement the logic for these operations in the authentication service that we will implement later to allow the user to register a new account and log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
    private final JwtService jwtService; //to inject the JwtService class that we will implement later in ...jwtService.java.. to handle the JWT token generation and validation for authentication and authorization in our application and then we will implement the logic for these operations in the authentication service that we will implement later to allow the user to register a new account and log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
    private final AuthenticationManager authenticationManager; //to inject the AuthenticationManager class that we will implement later in ...config.java.. to handle the authentication process for the user entity such as authenticating the user during login and then we will implement the logic for this operation in the authentication service that we will implement later to allow the user to log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if email already exists
        var existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email already registered");
        }
        
        //todo implement the logic for this method to allow the user to register a new account in our application and then it will return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application.
        var user = User.builder() //to create a new user object using the builder pattern and then we will set the user details such as email, password, first name, last name, and role from the register request that we will get from the client and then we will save this user to the database using the UserRepository that we will implement later and then it will return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application.
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user); //to generate a JWT token for the registered user using the JwtService that we will implement later to handle the JWT token generation and validation for authentication and authorization in our application and then it will return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application.
        return AuthenticationResponse.builder() //to return a response with the registered user details and a JWT
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //todo implement the logic for this method to allow the user to log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
        authenticationManager.authenticate( //to authenticate the user using the AuthenticationManager that we will implement later to handle the authentication process for the user entity such as authenticating the user during login and then we will implement the logic for this operation in the authentication service that we will implement later to allow the user to log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()) //to find the user by email from the database using the UserRepository that we will implement later to handle the database operations for the user entity such as saving a new user and finding a user by email and then we will implement the logic for these operations in the authentication service that we will implement later to allow the user to register a new account and log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user); //to generate a JWT token for the authenticated user using the JwtService that we will implement later to handle the JWT token generation and validation for authentication and authorization in our application and then it will return a response with the authenticated user details and a JWT token that can be used for authentication and authorization in our application.
        return AuthenticationResponse.builder() //to return a response with the authenticated user details and a JWT token.
                .token(jwtToken)
                .build();
    }
}
