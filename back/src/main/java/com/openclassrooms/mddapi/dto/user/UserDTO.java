package com.openclassrooms.mddapi.dto.user;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private List<TopicDTO> topics;

}