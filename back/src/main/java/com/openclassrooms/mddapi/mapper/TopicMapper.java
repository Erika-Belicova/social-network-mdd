package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import com.openclassrooms.mddapi.model.Topic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TopicMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /** Convert Topic entity to TopicDTO */
    public TopicDTO toTopicDTO(Topic topic) {
        return modelMapper.map(topic, TopicDTO.class);
    }

    /** Converts a list of topic entities to a list of TopicDTOs */
    public List<TopicDTO> toTopicDTOList(List<Topic> topics) {
        return topics.stream().map(this::toTopicDTO).collect(Collectors.toList());
    }

}
