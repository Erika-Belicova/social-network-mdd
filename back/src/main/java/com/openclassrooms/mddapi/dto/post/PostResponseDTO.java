package com.openclassrooms.mddapi.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponseDTO {

    private Long id;

    @JsonProperty("topic_id")
    private Long topicId;

    @JsonProperty("user_id")
    private Long userId;

    private String title;

    private String content;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime createdAt;

    private List<CommentResponseDTO> comments;

}