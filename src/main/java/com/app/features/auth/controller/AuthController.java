package com.app.features.auth.controller;

import com.app.features.auth.dto.AuthenticationRequestBody;
import com.app.features.auth.dto.AuthenticationResponseBody;
import com.app.features.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Controller for Authentication") // Swagger
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Login User",
            description = "Authenticate a user and return the authentication token along with user details.",
            //tags = {"Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication request with username and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationRequestBody.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication succeeded",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseBody.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"string\"}")
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseBody> login(@Valid @RequestBody AuthenticationRequestBody loginRequestBody) {
        return ResponseEntity.ok(authService.login(loginRequestBody));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register User",
            description = "Register a user and return a authentication token.",
            //tags = {"Authentication", "Register"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Register request with username and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationRequestBody.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseBody.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"string\"}")
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseBody> register(@Valid @RequestBody AuthenticationRequestBody registerRequestBody) {
        return ResponseEntity.ok(authService.register(registerRequestBody));
    }
}
