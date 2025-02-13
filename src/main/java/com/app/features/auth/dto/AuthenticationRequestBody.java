package com.app.features.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestBody {
    @NotBlank(message = "Email is mandatory")
    private String username;
    @NotBlank(message = "Email is mandatory")
    private String password;
}
