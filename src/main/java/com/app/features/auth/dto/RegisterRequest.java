package com.app.features.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String country;
    private String imageProfile;
    private String gender;
    private String occupation;
    private String descriptionProfile;
}
