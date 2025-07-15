package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.service.CommentService;import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("posts/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable("id") Long postId, @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        commentService.saveComment(userDTO.getId(), postId, commentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
