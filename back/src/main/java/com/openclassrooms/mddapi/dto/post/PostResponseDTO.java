package com.openclassrooms.mddapi.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostResponseDTO {

    final private Long id;

    @JsonProperty("topic_id")
    final private Long topicId;

    @JsonProperty("user_id")
    final private Long userId;

    final private String title;

    final private String content;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    final private LocalDateTime createdAt;

    final private List<CommentResponseDTO> comments;

}