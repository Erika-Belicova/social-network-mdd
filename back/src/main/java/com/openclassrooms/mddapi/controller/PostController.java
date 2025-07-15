package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import com.openclassrooms.mddapi.dto.post.PostRequestDTO;
import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.response.PostsResponse;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.PostService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Authorisation")
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public PostController (PostService postService, UserService userService,
                           CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsResponse> getUserPosts(Authentication authentication) {
        List<PostResponseDTO> posts = postService.getPostsForUserTopics(authentication.getName());
        PostsResponse postsResponse = new PostsResponse(posts);
        return ResponseEntity.ok(postsResponse);
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody @Valid PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        postService.savePost(userDTO.getId(), postRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable("id") Long postId) {
        PostResponseDTO postResponseDTO = postService.getPostById(postId);
        List<CommentResponseDTO> comments = commentService.getAllCommentsForPost(postId);
        postResponseDTO.setComments(comments);
        return ResponseEntity.ok(postResponseDTO);
    }

}
