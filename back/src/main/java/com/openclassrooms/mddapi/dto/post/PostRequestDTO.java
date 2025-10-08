package com.openclassrooms.mddapi.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO used for submitting a new post.
 */
@Data
public class PostRequestDTO {

    @NotNull(message = "Topic ID is required")
    @Schema(description = "ID of the topic the post belongs to", example = "3")
    private Long topicId;

    @NotBlank(message = "Title cannot be blank")
    @Schema(description = "Title of the post", example = "Understanding Java")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Schema(description = "Content of the post", example = "Java is a widely used programming language ...")
    private String content;

}