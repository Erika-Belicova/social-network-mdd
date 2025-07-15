package com.openclassrooms.mddapi.response;

import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class PostsResponse {

    private List<PostResponseDTO> posts;

    public PostsResponse(List<PostResponseDTO> posts) {
        this.posts = posts;
    }

}
