package com.app.features.auth.service;

import com.app.features.auth.dto.AuthResponse;
import com.app.features.auth.dto.LoginRequest;
import com.app.features.auth.dto.RegisterRequest;
import com.app.features.auth.model.Role;
import com.app.features.auth.model.User;
import com.app.features.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .imageProfile(request.getImageProfile())
                .gender(request.getGender())
                .occupation(request.getOccupation())
                .descriptionProfile(request.getDescriptionProfile())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return AuthResponse.builder().token(jwtService.getToken(user)).build();
    }
}
