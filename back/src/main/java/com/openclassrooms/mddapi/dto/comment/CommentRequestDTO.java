package com.openclassrooms.mddapi.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for submitting a new comment to a post.
 */
@Data
public class CommentRequestDTO {

    @NotBlank(message = "Comment content cannot be blank")
    @Schema(description = "The content of the comment", example = "This article was very interesting!")
    private String content;

}