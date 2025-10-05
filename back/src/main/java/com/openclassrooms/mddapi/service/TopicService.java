package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer handling business logic related to topics.
 * Provides operations to retrieve topic data from the repository
 * and map it to DTOs for external use.
 */
@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Autowired
    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    // topics are in the database so they need just one DTO to be exposed
    @Transactional(readOnly = true)
    public List<TopicDTO> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        return topicMapper.toTopicDTOList(topics);
    }

}
