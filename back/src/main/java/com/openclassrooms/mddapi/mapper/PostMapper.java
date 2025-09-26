package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.post.PostRequestDTO;
import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import com.openclassrooms.mddapi.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper component for converting between Post entities and their DTO representations.
 */
@Component
public class PostMapper {

    private final ModelMapper modelMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PostMapper(ModelMapper modelMapper, CommentMapper commentMapper) {
        this.modelMapper = modelMapper;
        this.commentMapper = commentMapper;
    }

    /** Convert Post entity to PostResponseDTO */
    public PostResponseDTO toPostResponseDTO(Post post) {
        PostResponseDTO postResponseDTO = modelMapper.map(post, PostResponseDTO.class);

        // set user information if available
        if (post.getUser() != null) {
            postResponseDTO.setUsername(post.getUser().getUsername());
            postResponseDTO.setUserId(post.getUser().getId());
        }

        // set topic information if available
        if (post.getTopic() != null) {
            postResponseDTO.setTopicId(post.getTopic().getId());
            postResponseDTO.setTopicTitle(post.getTopic().getTitle());
        }

        // map comments associated with the post
        if (post.getComments() != null) {
            postResponseDTO.setComments(commentMapper.toCommentResponseDTOList(post.getComments()));
        }

        return postResponseDTO;
    }

    /** Convert PostRequestDTO to Post entity */
    public Post toPostEntity(PostRequestDTO postRequestDTO) {
        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        return post;
    }

    /** Converts a list of post entities to a list of PostResponseDTOs */
    public List<PostResponseDTO> toPostResponseDTOList(List<Post> posts) {
        // map each post entity to a PostResponseDTO and collect into a list
        return posts.stream().map(this::toPostResponseDTO).collect(Collectors.toList());
    }
}
