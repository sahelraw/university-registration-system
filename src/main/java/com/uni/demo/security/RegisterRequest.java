package com.uni.demo.security;

public record RegisterRequest(String username, String password, AppUserRole role) {
}
