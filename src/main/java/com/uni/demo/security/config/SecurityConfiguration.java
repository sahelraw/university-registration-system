package com.uni.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf().disable()

            .authorizeHttpRequests()

            // ================= AUTH =================
            .requestMatchers("/api/v1/auth/**").permitAll()

            // ================= STUDENT =================
            .requestMatchers(HttpMethod.POST, "/api/v1/student/studentAdd").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/student/studentAll").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/student/**").hasAnyAuthority("ADMIN", "STUDENT")
            .requestMatchers(HttpMethod.PUT, "/api/v1/student/**").hasAnyAuthority("ADMIN", "STUDENT")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/student/**").hasAuthority("ADMIN")

            // ================= TEACHER =================
            .requestMatchers(HttpMethod.POST, "/api/v1/teacher/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/teacher/**").hasAnyAuthority("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/teacher/**").hasAnyAuthority("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/teacher/**").hasAuthority("ADMIN")

            // ================= MAJOR =================
            .requestMatchers(HttpMethod.POST, "/api/v1/major/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/major/**").hasAnyAuthority("ADMIN", "STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/major/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/major/**").hasAuthority("ADMIN")

            // ================= COURSE =================
            .requestMatchers(HttpMethod.POST, "/api/v1/course/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/course/**").hasAnyAuthority("STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/course/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/course/**").hasAuthority("ADMIN")

            // ================= SECTION =================
            .requestMatchers(HttpMethod.POST, "/api/v1/section/**").hasAnyAuthority("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.GET, "/api/v1/section/**").hasAnyAuthority("STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/section/**").hasAnyAuthority("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/section/**").hasAnyAuthority("ADMIN", "TEACHER")

            // ================= ENROLLMENT =================
            .requestMatchers(HttpMethod.POST, "/api/v1/enrollment/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/enrollment/**").hasAnyAuthority("ADMIN", "STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/enrollment/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/enrollment/**").hasAuthority("ADMIN")

            // ================= ANY OTHER =================
            .anyRequest().authenticated()

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authenticationProvider(authenticationProvider)

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}