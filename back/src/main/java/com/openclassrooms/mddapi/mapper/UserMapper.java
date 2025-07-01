package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.user.RegisterRequestDTO;
import com.openclassrooms.mddapi.dto.user.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /** Convert User entity to UserDTO */
    public UserDTO toUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
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