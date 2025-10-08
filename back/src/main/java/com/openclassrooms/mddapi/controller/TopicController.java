package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.topic.TopicDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing topics and user topic subscriptions.
 */
@Tag(name = "Topics", description = "Endpoints for topic operations")
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

    /**
     * Retrieves a list of all available topics.
     *
     * @return list of topic DTOs
     */
    @Operation(summary = "Get all topics", description = "Retrieve a list of all available topics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of topics returned successfully")
    })
    @GetMapping("/topics")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<TopicDTO> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    /**
     * Subscribes the authenticated user to a specific topic.
     *
     * @param topicId ID of the topic to subscribe to
     * @return HTTP 201 Created if the subscription is successful
     */
    @Operation(summary = "Subscribe to topic", description = "Subscribe the user to a topic by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully subscribed to topic"),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @PostMapping("/topics/{id}/subscriptions")
    public ResponseEntity<Void> subscribeToTopic(@PathVariable("id") Long topicId) {
        // get currently authenticated user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        userService.subscribe(userDTO.getId(), topicId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Unsubscribes the authenticated user from a specific topic.
     *
     * @param topicId ID of the topic to unsubscribe from
     * @return HTTP 200 OK if the unsubscription is successful
     */
    @Operation(summary = "Unsubscribe from topic", description = "Unsubscribe the user from a topic by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully unsubscribed from topic"),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @DeleteMapping("/topics/{id}/subscriptions")
    public ResponseEntity<Void> unsubscribeFromTopic(@PathVariable("id") Long topicId) {
        // get currently authenticated user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        userService.unsubscribe(userDTO.getId(), topicId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
