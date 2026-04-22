package com.uni.demo.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token; //to store the JWT token that will be returned to the client after successful authentication and authorization and then it will be used by the client to authenticate and authorize the user in subsequent requests to access protected resources in our application.
}
