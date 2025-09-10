package com.yourpackage.mello.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SignupResponse {
 
    private long id;
    private String username;
}
