package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.user.RegisterRequestDTO;
import com.openclassrooms.mddapi.dto.user.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        User user = userMapper.toUserEntity(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserDTO updateUser(Long userId, UpdateUserDTO updateUserDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        userMapper.updateUserEntityFromDTO(updateUserDTO, existingUser);
        existingUser.setPassword((passwordEncoder.encode(updateUserDTO.getPassword())));
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByCredential(String credential) {
        User user = userRepository.findByUsername(credential)
                .or(() -> userRepository.findByEmail(credential))
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        return userMapper.toUserDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

}