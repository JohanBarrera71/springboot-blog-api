package com.app.features.auth.service;

import com.app.features.auth.dto.AuthenticationRequestBody;
import com.app.features.auth.dto.AuthenticationResponseBody;
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

    public AuthenticationResponseBody login(AuthenticationRequestBody loginRequestBody) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestBody.getUsername(), loginRequestBody.getPassword()));
        UserDetails user = userRepository.findByUsername(loginRequestBody.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        String token = jwtService.getToken(user);
        return AuthenticationResponseBody.builder().token(token).message("Authentication succeeded.").build();
    }

    public AuthenticationResponseBody register( AuthenticationRequestBody registerRequestBody) {
        User user = User.builder()
                .username(registerRequestBody.getUsername())
                .password(passwordEncoder.encode(registerRequestBody.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return AuthenticationResponseBody.builder().token(jwtService.getToken(user)).message("User registered successfully.").build();
    }
}
