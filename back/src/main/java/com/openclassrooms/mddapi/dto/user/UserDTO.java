package com.openclassrooms.mddapi.dto.user;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * DTO representing a user with profile details and their associated topics.
 */
@Data
public class UserDTO {

    @Schema(description = "Unique identifier of the user", example = "12")
    private Long id;

    @Schema(description = "Username of the user", example = "johnsmith")
    private String username;

    @Schema(description = "Email address of the user", example = "john@smith.com")
    private String email;

    @Schema(description = "List of topics associated with the user")
    private List<TopicDTO> topics;

}