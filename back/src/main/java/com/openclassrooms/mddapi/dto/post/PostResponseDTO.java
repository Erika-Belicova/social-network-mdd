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

    private final Long id;

    @JsonProperty("topic_id")
    private final Long topicId;

    @JsonProperty("user_id")
    private final Long userId;

    private final String title;

    private final String content;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    final private LocalDateTime createdAt;

    private final List<CommentResponseDTO> comments;

}