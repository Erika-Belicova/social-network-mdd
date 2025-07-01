package com.openclassrooms.mddapi.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentResponseDTO {

    private final Long id;

    @JsonProperty("post_id")
    private final Long postId;

    @JsonProperty("user_id")
    private final Long userId;

    private final String username;

    private final String content;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy/MM/dd")
    final private LocalDateTime createdAt;

}