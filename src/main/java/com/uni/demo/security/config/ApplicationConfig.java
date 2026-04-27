package com.uni.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import lombok.RequiredArgsConstructor; //to generate a constructor with required arguments for the fields that are final or marked with @NonNull
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; //to use the BCrypt hashing algorithm
import com.uni.demo.security.user.UserRepository;



@Configuration
@RequiredArgsConstructor //in case we want to rject something
public class ApplicationConfig {

    private final UserRepository repository; //to inject the UserRepository interface that we will implement later to interact with the database to perform CRUD operations on the user entity and we will use this repository in the UserDetailsService to load the user details from the database and we will use this service in the JwtAuthenticationFilter to authenticate the user using JWT (JSON Web Token) and then we will implement the JwtAuthorizationFilter.java that will be responsible for authorizing the user based on their roles and permissions and then we will implement SecurityConfig.java that will be responsible for configuring the security of our application and then we will implement AuthController.java that will be responsible for handling the authentication and authorization requests and then we will implement UserService.java that will be responsible for handling the business logic related to the user entity and then we will implement UserRepository.java that will be responsible for interacting with the database to perform CRUD operations on the user entity.

    @Bean //load user-specific data during the authentication process
    public UserDetailsService userDetailsService() {
           return username -> repository.findByEmail(username) //we will implement this method later to load the user details from the database using the username which is the email in our case and we will use this service in the JwtAuthenticationFilter to authenticate the user using JWT (JSON Web Token) and then we will implement the JwtAuthorizationFilter.java that will be responsible for authorizing the user based on their roles and permissions and then we will implement SecurityConfig.java that will be responsible for configuring the security of our application and then we will implement AuthController.java that will be responsible for handling the authentication and authorization requests and then we will implement UserService.java that will be responsible for handling the business logic related to the user entity and then we will implement UserRepository.java that will be responsible for interacting with the database to perform CRUD operations on the user entity.
           .orElseThrow(() -> new UsernameNotFoundException("User not found")); //to throw an exception if the user is not found in the database
        }

        @Bean //actual logic of validating user credentials
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); //to create an instance of the DaoAuthenticationProvider class which is a Spring Security class that provides authentication using a UserDetailsService and a PasswordEncoder and we will use this authentication provider in the security configuration to authenticate the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
            authProvider.setUserDetailsService(userDetailsService()); //to set the UserDetailsService to
            authProvider.setPasswordEncoder(passwordEncoder()); //to set the PasswordEncoder to the BCryptPasswordEncoder that we will implement later to encode the password of the user and to validate the password during authentication and we will use this authentication provider in the security configuration to authenticate the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
            return authProvider; //to return the authentication provider to be used in the security configuration to
        }

        @Bean //perform auth
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager(); //to return the AuthenticationManager from the AuthenticationConfiguration which is a Spring Security class that provides the authentication manager to be used in the security configuration to authenticate the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(); //to return an instance of the BCryptPasswordEncoder class which is a Spring Security class that provides password encoding using the BCrypt hashing algorithm and we will use this password encoder to encode the password of the user and to validate the password during authentication and we will use this authentication provider in the security configuration to authenticate the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
        }
}

