package com.uni.demo.security.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth") //to set the base URL for the authentication controller to be "/api/v1/auth" and then we will implement the endpoints for authentication and authorization such as "/register" for registering a new user and "/login" for logging in a user and then we will implement the logic for these endpoints in the authentication service that we will implement later.
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService service; //to inject the AuthenticationService class that we will implement later in ...authService.java.. to handle the business logic for authentication and authorization such as registering a new user and logging in a user and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
    
    @PostMapping("/register") //to set the URL for the register endpoint to be "/register" and then we will implement the logic for this endpoint in the authentication service that we will implement later to allow the user to register a new account in our application and then it will return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application.
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request //to get the register request body from the client which will contain the user details such as email, password, first name, last name, and role and then we will implement the logic for this endpoint in the authentication service that we will implement later to allow the user to register a new account in our application and then it will return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application.
    ) {
        //todo implement the logic for this endpoint in the authentication service that we will implement later to allow the user to register a new account in our application and then it will return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application.
    return ResponseEntity.ok(service.register(request)); //to return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application and we will implement the logic for this endpoint in the authentication service that we will implement later to allow the user to register a new account in our application and then it will return a response with the registered user details and a JWT token that can be used for authentication and authorization in our application.
    }

    @PostMapping("/authenticate") //to set the URL for the login endpoint to be "/login" and then we will implement the logic for this endpoint in the authentication service that we will implement later to allow the user to log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request //to get the authentication request body from the client which will contain the user details such as email and password and then we will implement the logic for this endpoint in the authentication service that we will implement later to allow the user to log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
    ) {
        //todo implement the logic for this endpoint in the authentication service that we will implement later to allow the user to log in to their account in our application and then it will return a response with the user details and a JWT token that can be used for authentication and authorization in our application.
        return ResponseEntity.ok(service.authenticate(request));
    }
}
//implent the response in authresponse.java
//implement the request in RegisterRequest.java