package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequestDTO {

    @NotNull
    private Long topicId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

}