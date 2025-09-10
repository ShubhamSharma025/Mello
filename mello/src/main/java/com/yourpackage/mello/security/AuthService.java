package com.yourpackage.mello.security;

import com.yourpackage.mello.model.Role;
import com.yourpackage.mello.model.User;
import com.yourpackage.mello.repository.RoleRepository;
import com.yourpackage.mello.repository.UserRepository;

import java.util.HashSet;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository=roleRepository;
    }

    // ✅ Login
      public LoginResponse login(LoginRequest request){
        Authentication authentication=authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user=(User) authentication.getPrincipal();

        String token= jwtService.generateToken(user);
        return new LoginResponse(token,user.getId());
      }

   

 public SignupResponse signup(SignupRequest signupRequest) {
    if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
        throw new IllegalArgumentException("USER ALREADY EXISTS");
    }

    // encode password
    User user = User.builder()
            .username(signupRequest.getUsername())
            .password(passwordEncoder.encode(signupRequest.getPassword()))
            .roles(new HashSet<>())
            .build();

    // assign default role USER
    Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Role USER not found"));
    user.getRoles().add(userRole);

    
    user = userRepository.save(user);

    return new SignupResponse(user.getId(), user.getUsername());
}
}

