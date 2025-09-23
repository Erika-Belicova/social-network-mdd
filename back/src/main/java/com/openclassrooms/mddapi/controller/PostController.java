package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import com.openclassrooms.mddapi.dto.post.PostRequestDTO;
import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.response.PostsResponse;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.PostService;
import com.openclassrooms.mddapi.service.UserService;
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

import java.util.List;

/**
 * Controller for handling post creation, retrieval, and viewing with comments.
 */
@Tag(name = "Posts", description = "Endpoints for post operations")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, UserService userService,
                          CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    /**
     * Retrieves posts from the topics the authenticated user is subscribed to.
     *
     * @param authentication the authentication token
     * @return a list of posts in a PostsResponse wrapper
     */
    @Operation(summary = "Get user's feed", description = "Get posts from the topics the authenticated user is subscribed to.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user's feed")
    @GetMapping("/posts")
    public ResponseEntity<PostsResponse> getUserPosts(Authentication authentication) {
        List<PostResponseDTO> posts = postService.getPostsForUserTopics(authentication.getName());
        PostsResponse postsResponse = new PostsResponse(posts);
        return ResponseEntity.ok(postsResponse);
    }

    /**
     * Allows the authenticated user to create a new post.
     *
     * @param postRequestDTO the post content and topic id
     * @return HTTP 201 Created if successful
     */
    @Operation(summary = "Create a new post", description = "Create a post for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "A post with this title already exists")
    })
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        PostResponseDTO createdPost = postService.savePost(userDTO.getId(), postRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * Retrieves a post by its ID along with associated comments.
     *
     * @param postId the ID of the post
     * @return the post data with comments
     */
    @Operation(summary = "Get post by ID", description = "Retrieve a single post and its comments by post ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable("id") Long postId) {
        PostResponseDTO postResponseDTO = postService.getPostById(postId);
        List<CommentResponseDTO> comments = commentService.getAllCommentsForPost(postId);
        postResponseDTO.setComments(comments);
        return ResponseEntity.ok(postResponseDTO);
    }

}
