package com.openclassrooms.mddapi.response;

import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Response wrapper containing a list of posts.
 */
@Data
@Schema(description = "Response object containing a list of posts")
public class PostsResponse {

    @Schema(description = "List of posts")
    private List<PostResponseDTO> posts;

    public PostsResponse(List<PostResponseDTO> posts) {
        this.posts = posts;
    }

}
