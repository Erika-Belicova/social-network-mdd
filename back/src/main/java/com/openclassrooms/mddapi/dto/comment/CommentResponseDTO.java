package com.openclassrooms.mddapi.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO representing a comment returned in API responses.
 */
@Data
public class CommentResponseDTO {

    @Schema(description = "Unique identifier of the comment", example = "42")
    private Long id;

    @JsonProperty("post_id")
    @Schema(description = "ID of the post the comment belongs to", example = "10")
    private Long postId;

    @JsonProperty("user_id")
    @Schema(description = "ID of the user who made the comment", example = "5")
    private Long userId;

    @Schema(description = "Username of the commenter", example = "johnsmith")
    private String username;

    @Schema(description = "Content of the comment", example = "Really interesting post!")
    private String content;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    @Schema(description = "Date when the comment was created", example = "2025/07/07")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    @Schema(description = "Date when the comment was last updated", example = "2025/07/07")
    private LocalDateTime updatedAt;

}