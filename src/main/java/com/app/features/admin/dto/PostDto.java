package com.app.features.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String description;
    @NotBlank(message = "Content is mandatory")
    private String content;
    private int duration;
    private List<LabelDto> labels;
}
