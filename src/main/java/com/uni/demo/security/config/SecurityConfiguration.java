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
            .requestMatchers(HttpMethod.POST, "/api/v1/student/studentAdd").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/student/studentAll").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/student/**").hasAnyRole("ADMIN", "STUDENT")
            .requestMatchers(HttpMethod.PUT, "/api/v1/student/**").hasAnyRole("ADMIN", "STUDENT")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/student/**").hasRole("ADMIN")

            // ================= TEACHER =================
            .requestMatchers(HttpMethod.POST, "/api/v1/teacher/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/teacher/teacherAll").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/teacher/**").hasAnyRole("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/teacher/**").hasAnyRole("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/teacher/**").hasRole("ADMIN")

            // ================= MAJOR =================
            .requestMatchers(HttpMethod.POST, "/api/v1/major/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/major/majorAll").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/major/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/major/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/major/**").hasRole("ADMIN")

            // ================= COURSE =================
            .requestMatchers(HttpMethod.POST, "/api/v1/course/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/course/courseAll").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/course/**").hasAnyRole("STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/course/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/course/**").hasRole("ADMIN")

            // ================= SECTION =================
            .requestMatchers(HttpMethod.POST, "/api/v1/section/**").hasAnyRole("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.GET, "/api/v1/section/sectionAll").hasAnyRole("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.GET, "/api/v1/section/**").hasAnyRole("STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/section/**").hasAnyRole("ADMIN", "TEACHER")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/section/**").hasAnyRole("ADMIN", "TEACHER")

            // ================= ENROLLMENT =================
            .requestMatchers(HttpMethod.POST, "/api/v1/enrollment/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/student/enrollmentAll").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/enrollment/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
            .requestMatchers(HttpMethod.PUT, "/api/v1/enrollment/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/enrollment/**").hasRole("ADMIN")

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