package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import com.openclassrooms.mddapi.dto.user.RegisterRequestDTO;
import com.openclassrooms.mddapi.dto.user.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.model.User;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        // skip password mapping for security reasons as it will be encoded in the service
        skipPasswordMapping(RegisterRequestDTO.class);
        skipPasswordMapping(UpdateUserDTO.class);
    }

    /** Configures ModelMapper to skip mapping the password field for the given source DTO class */
    private void skipPasswordMapping(Class<?> sourceType) {
        modelMapper.typeMap(sourceType, User.class)
                .addMappings(mapper -> mapper.skip(User::setPassword));
    }

    /** Convert User entity to UserDTO */
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        // map topics from entity to DTOs
        if (user.getTopics() != null) {
            List<TopicDTO> topicDTOs = user.getTopics()
                    .stream()
                    .map(topic -> modelMapper.map(topic, TopicDTO.class))
                    .collect(Collectors.toList());
            userDTO.setTopics(topicDTOs);
        }
        return userDTO;
    }

    /** Convert RegisterRequestDTO to User entity */
    public User toUserEntity(RegisterRequestDTO registerRequestDTO) {
        return modelMapper.map(registerRequestDTO, User.class);
    }

    /** Update existing User entity with fields from UpdateUserDTO */
    public void updateUserEntityFromDTO(UpdateUserDTO updateUserDTO, User existingUser) {
        modelMapper.map(updateUserDTO, existingUser);
    }

}