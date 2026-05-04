package com.uni.demo.security.auth;

import com.uni.demo.security.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {


    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Role role;

}
