package com.uni.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter; //to inject the JwtAuthenticationFilter class that we will implement later to authenticate the user using JWT (JSON Web Token) and then we will implement the JwtAuthorizationFilter.java that will be responsible for authorizing the user based on their roles and permissions and then we will implement SecurityConfig.java that will be responsible for configuring the security of our application and then we will implement AuthController.java that will be responsible for handling the authentication and authorization requests and then we will implement UserService.java that will be responsible for handling the business logic related to the user entity and then we will implement UserRepository.java that will be responsible for interacting with the database to perform CRUD operations on the user entity.
    private final AuthenticationProvider authenticationProvider; //to inject the AuthenticationProvider interface that we will implement later to authenticate the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable() //to disable CSRF protection because we are using JWT token for authentication and we will not use cookies for authentication so we dont need CSRF protection
            .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/register")// "/api/v1/auth/authenticate")
                .permitAll() //to allow all requests to the authentication endpoints without authentication because we need to allow the user to register and login without authentication
                .anyRequest()
                .authenticated() //to require authentication for all other requests to secure the endpoints of our application and to allow only authenticated users to access the protected resources of our application
                .and() //to continue configuring the security of our application and to allow us to add more configurations such as adding the JWT authentication filter and the JWT authorization filter to the security filter chain to authenticate and authorize the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //to set the session creation policy to
                .and()
                .authenticationProvider(authenticationProvider) //to set the authentication provider to the custom authentication provider that we will implement later in "appconfig" to authenticate the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //to add the

                return http.build(); //to build the security filter chain and return it to be used by the Spring Security framework to secure our application and to allow us to authenticate and authorize the user using JWT (JSON Web Token) and then it will allow the request to proceed to the next filter in the chain which will be the controller that will handle the request and return a response.
            }
}
