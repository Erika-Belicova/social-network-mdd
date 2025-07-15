package com.openclassrooms.mddapi.response;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import lombok.Data;

import java.util.List;

@Data
public class TopicsResponse {

    private List<TopicDTO> topics;

    public TopicsResponse(List<TopicDTO> topics) {
        this.topics = topics;
    }

}