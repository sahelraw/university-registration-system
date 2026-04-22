package com.uni.demo.security.config;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //to extend the OncePerRequestFilter class which is a Spring Security filter that ensures that the filter is only executed once per request "every time we request" and we will implement this filter to authenticate the user using JWT (JSON Web Token) and then we will implement the JwtAuthorizationFilter.java that will be responsible for authorizing the user based on their roles and permissions and then we will implement SecurityConfig.java that will be responsible for configuring the security of our application and then we will implement AuthController.java that will be responsible for handling the authentication and authorization requests and then we will implement UserService.java that will be responsible for handling the business logic related to the user entity and then we will implement UserRepository.java that will be responsible for interacting with the database to perform CRUD operations on the user entity.

private final JwtService jwtService; //to inject the JwtService class that we will implement later to handle the JWT (JSON Web Token) operations such as generating and validating the JWT token and extracting the user details from the JWT token and we will use this service in this filter to authenticate the user using JWT (JSON Web Token) and then we will implement the JwtAuthorizationFilter.java that will be responsible for authorizing the user based on their roles and permissions and then we will implement SecurityConfig.java that will be responsible for configuring the security of our application and then we will implement AuthController.java that will be responsible for handling the authentication and authorization requests and then we will implement UserService.java that will be responsible for handling the business logic related to the user entity and then we will implement UserRepository.java that will be responsible for interacting with the database to perform CRUD operations on the user entity.
private final UserDetailsService userDetailsService; //to inject the UserDetailsService interface that we will implement later to load the user details from the database and we will use this service in this filter to authenticate the user using JWT (JSON Web Token) and then we will implement the JwtAuthorizationFilter.java that will be responsible for authorizing the user based on their roles and permissions and then we will implement SecurityConfig.java that will be responsible for configuring the security of our application and then we will implement AuthController.java that will be responsible for handling the authentication and authorization requests and then we will implement UserService.java that will be responsible for handling the business logic related to the user entity and then we will implement UserRepository.java that will be responsible for interacting with the database to perform CRUD operations on the user entity.


@Override
protected void doFilterInternal(
    @NonNull HttpServletRequest request, //NonNull is to remove the warning about them being null because they shouldnt be null and to make sure they are not null
    @NonNull HttpServletResponse response, 
    @NonNull FilterChain filterChain
) throws ServletException, IOException {
//checking if it have JWt token
final String authHeader = request.getHeader("Authorization"); //to get the Authorization header from the request which contain the JWT token
final String jwt;
final String userEmail;
//now for the check implementation
if (authHeader == null || !authHeader.startsWith("Bearer ")) { //to check if the Authorization header is null or does not start with "Bearer " which is the prefix for the JWT token in the Authorization header
    filterChain.doFilter(request, response); //if the Authorization header is null or does not start with "Bearer " we will just continue with the filter chain without doing anything because it means that the request is not authenticated and we will let the next filter in the chain to handle the request and return a response
    return; //to return from the method because we dont want to continue with the rest of the code in this method because it means that the request is not authenticated
}
jwt = authHeader.substring(7); //to get the JWT token from the Authorization header by removing the "Bearer " prefix which is 7 characters long
userEmail = jwtService.extractUsername(jwt); //todo exract the userEmail from JWT token using the JWT class that we will implement
if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) { //to check if the userEmail is not null which means that the JWT token is valid and we can extract the user details from it and authenticate the user
    //todo authenticate the user using the userEmail and the JWT token and set the authentication in the security context to allow the request to proceed to the next filter in the chain which will be the authorization filter that will authorize the user based on their roles and permissions and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); //to load the user details from the database using the userEmail which is the username for authentication and we will implement the UserDetailsService interface later to load the user details from the database and we will use this service in this filter to authenticate the user using JWT (JSON Web Token) and then we will implement the JwtAuthorizationFilter.java that will be responsible for authorizing the user based on their roles and permissions and then we will implement SecurityConfig.java that will be responsible for configuring the security of our application and then we will implement AuthController.java that will be responsible for handling the authentication and authorization requests and then we will implement UserService.java that will be responsible for handling the business logic related to the user entity and then we will implement UserRepository.java that will be responsible for interacting with the database to perform CRUD operations on the user entity.
    if (jwtService.isTokenValid(jwt, userDetails)) { //to check if the JWT token is valid by checking if the userEmail in the JWT token is the same as the userEmail in the user details and also to check if the JWT token is not expired and we will implement this method later in the JwtService class to validate the JWT token
    //todo set the authentication in the security context to allow the request to proceed to the next filter in the chain which will be the authorization filter that will authorize the user based on their roles and permissions and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null, //we will not use the credentials in this case because we are using JWT token for authentication and we have already validated the JWT token so we dont need to use the credentials for authentication
            userDetails.getAuthorities() //to set the authorities of the user in the authentication token to allow the authorization filter to authorize the user based on their roles and permissions and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
        );
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        ); //to set the details of the authentication token to include the details of the request such as the remote address and the session ID and we will use this details in the authorization filter to authorize the user based on their roles and permissions and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
        SecurityContextHolder.getContext().setAuthentication(authToken); //to set the authentication in the security context to allow the request to proceed to the next filter in the chain which will be the authorization filter that will authorize the user based on their roles and permissions and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
    }
}
filterChain.doFilter(request, response); //to continue with the filter chain to allow the request to proceed to the next filter in the chain which will be the authorization filter that will authorize the user based on their roles and permissions and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
}
}
