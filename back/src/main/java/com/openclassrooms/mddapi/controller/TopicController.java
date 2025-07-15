package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api")
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;

    @Autowired
    public TopicController(TopicService topicService, UserService userService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    @GetMapping("/topics")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<TopicDTO> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    @PostMapping("/topics/{id}/subscriptions")
    public ResponseEntity<Void> subscribeToTopic(@PathVariable("id") Long topicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        userService.subscribe(userDTO.getId(), topicId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/topics/{id}/subscriptions")
    public ResponseEntity<Void> unsubscribeFromTopic(@PathVariable("id") Long topicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        userService.unsubscribe(userDTO.getId(), topicId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
