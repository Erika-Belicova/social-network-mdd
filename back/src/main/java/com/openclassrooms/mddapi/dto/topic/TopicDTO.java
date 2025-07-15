package com.openclassrooms.mddapi.dto.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO representing a topic with its details.
 */
@Data
public class TopicDTO {

    @Schema(description = "Unique identifier of the topic", example = "1")
    private Long id;

    @Schema(description = "Title of the topic", example = "Java Programming")
    private String title;

    @Schema(description = "Detailed description of the topic", example = "All about Java programming language, tutorials, and best practices.")
    private String description;

}
