package com.openclassrooms.mddapi.response;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Response wrapper containing a list of topics.
 */
@Data
@Schema(description = "Response object containing a list of topics")
public class TopicsResponse {

    @Schema(description = "List of topics")
    private List<TopicDTO> topics;

    public TopicsResponse(List<TopicDTO> topics) {
        this.topics = topics;
    }

}