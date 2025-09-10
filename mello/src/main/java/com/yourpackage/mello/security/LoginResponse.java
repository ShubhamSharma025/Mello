package com.yourpackage.mello.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class LoginResponse {
    private String  jwt;
    private Long userId;
}
