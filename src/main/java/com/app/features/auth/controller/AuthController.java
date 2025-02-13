package com.app.features.auth.controller;

import com.app.features.auth.dto.AuthenticationRequestBody;
import com.app.features.auth.dto.AuthenticationResponseBody;
import com.app.features.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor // Constructor de la clase
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseBody> login(@Valid @RequestBody AuthenticationRequestBody loginRequestBody) {
        return ResponseEntity.ok(authService.login(loginRequestBody));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseBody> register(@Valid @RequestBody AuthenticationRequestBody registerRequestBody) {
        return ResponseEntity.ok(authService.register(registerRequestBody));
    }
}
