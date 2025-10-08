package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.user.RegisterRequestDTO;
import com.openclassrooms.mddapi.dto.user.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.exception.DuplicateFieldValidationException;
import com.openclassrooms.mddapi.exception.TopicNotFoundException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer managing user-related operations such as registration,
 * profile updates, retrieving user data, and managing topic subscriptions.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, TopicRepository topicRepository,
                       UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        // check uniqueness of username/email and encode password before saving new user
        if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
            throw new DuplicateFieldValidationException("Username is already in use");
        }

        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new DuplicateFieldValidationException("Email is already in use");
        }

        User user = userMapper.toUserEntity(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserDTO updateUser(String username, UpdateUserDTO updateUserDTO) {
        User existingUser = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // verify that username is unique
        if (!existingUser.getUsername().equals(updateUserDTO.getUsername()) &&
                userRepository.existsByUsername(updateUserDTO.getUsername())) {
            throw new DuplicateFieldValidationException("Username is already in use");
        }

        // verify that email is unique
        if (!existingUser.getEmail().equals(updateUserDTO.getEmail()) &&
                userRepository.existsByEmail(updateUserDTO.getEmail())) {
            throw new DuplicateFieldValidationException("Email is already in use");
        }

        // update username and email
        userMapper.updateUserEntityFromDTO(updateUserDTO, existingUser);

        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }
        User savedUser = userRepository.save(existingUser);
        return userMapper.toUserDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByCredential(String credential) {
        // retrieve user by username or email
        User user = userRepository.findByUsernameOrEmail(credential, credential)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Transactional
    public void subscribe(Long userId, Long topicId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found"));

        // add topic subscription if not already subscribed
        if (!user.getTopics().contains(topic)) {
            user.getTopics().add(topic);
            userRepository.save(user);
        }
    }

    @Transactional
    public void unsubscribe(Long userId, Long topicId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found"));

        // remove topic subscription if currently subscribed
        if (user.getTopics().contains(topic)) {
            user.getTopics().remove(topic);
            userRepository.save(user);
        }
    }

}