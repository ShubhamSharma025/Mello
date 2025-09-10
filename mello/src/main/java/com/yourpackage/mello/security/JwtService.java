package com.yourpackage.mello.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.yourpackage.mello.model.Role;
import com.yourpackage.mello.model.User;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtsecretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;


    private SecretKey  getSecretKey(){
        return Keys.hmacShaKeyFor(jwtsecretKey.getBytes(StandardCharsets.UTF_8));       
    }

    // ✅ Generate token with username + roles
    public String generateToken(User user) {

        List<String>roles = user.getRoles() .stream()
               .map(Role::getName)
               .toList();
       
               return Jwts.builder()
                .setSubject(user.getUsername()) // who the token is for
                .claim("roles", roles) // attach roles to claims
                .setIssuedAt(new Date(System.currentTimeMillis())) // issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // expiry
                .signWith(getSecretKey(),SignatureAlgorithm.HS256) // signature
                .compact();
    }

    // ✅ Extract username
    public String extractUsername(String token) {
         Claims claims= Jwts.parserBuilder()
         . setSigningKey(getSecretKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
          
          return claims.getSubject();
    }

    // ✅ Extract roles
   public List<String> extractRoles(String token) {
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())   // use your signing key
            .build()
            .parseClaimsJws(token)           // or parseSignedClaims(token) if using 0.12.x+
            .getBody();

    return claims.get("roles", List.class);  // extract roles
}

    // ✅ Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username=extractUsername(token) ;
         return username.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }

    // --- helpers ---
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {

        if(token==null || token.trim().isEmpty()){
            throw new IllegalArgumentException("JWT token is missing");
        }
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // private SecretKey getSignInKey() {
    //    return Keys.hmacShaKeyFor(jwtsecretKey.getBytes(StandardCharsets.UTF_8));

    // }
}
