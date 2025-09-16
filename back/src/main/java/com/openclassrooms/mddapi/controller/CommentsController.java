package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDTO;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.service.CommentService;import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing comments on posts.
 */
@Tag(name = "Comments", description = "Endpoints for comment operations")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api")
public class CommentsController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentsController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    /**
     * Adds a comment to a post identified by its ID.
     *
     * @param postId             the ID of the post to comment on
     * @param commentRequestDTO  the comment data to be saved
     * @return a response entity with HTTP status 201 (Created) if successful
     */
    @Operation(summary = "Add comment to post", description = "Add a comment to a post by ID as the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment data"),
            @ApiResponse(responseCode = "404", description = "User or Post not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("posts/{id}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(@PathVariable("id") Long postId, @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        CommentResponseDTO createdComment = commentService.saveComment(userDTO.getId(), postId, commentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

}
