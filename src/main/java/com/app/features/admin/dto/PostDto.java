package com.app.features.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String title;
    private String description;
    private String content;
    private int duration;
    private List<LabelDto> labels;
}
