package com.app.features.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String description;
}
