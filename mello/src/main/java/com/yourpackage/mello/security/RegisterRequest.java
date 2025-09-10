package com.yourpackage.mello.security;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}