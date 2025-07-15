package com.openclassrooms.mddapi.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing a post returned in API responses, including associated comments.
 */
@Data
public class PostResponseDTO {

    @Schema(description = "Unique identifier of the post", example = "15")
    private Long id;

    @JsonProperty("topic_id")
    @Schema(description = "ID of the topic the post belongs to", example = "3")
    private Long topicId;

    @JsonProperty("user_id")
    @Schema(description = "ID of the user who created the post", example = "7")
    private Long userId;

    @Schema(description = "Title of the post", example = "Introduction to Spring Boot")
    private String title;

    @Schema(description = "Content of the post", example = "Spring Boot simplifies the setup of Spring applications...")
    private String content;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    @Schema(description = "Date when the post was created", example = "2025/07/07")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    @Schema(description = "Date when the post was last updated", example = "2025/07/08")
    private LocalDateTime updatedAt;

    @Schema(description = "List of comments associated with the post")
    private List<CommentResponseDTO> comments;

}