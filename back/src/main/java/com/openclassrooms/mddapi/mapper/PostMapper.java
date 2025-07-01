package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.post.PostRequestDTO;
import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public PostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /** Convert Post entity to PostResponseDTO */
    public PostResponseDTO toPostResponseDTO(Post post) {
        return modelMapper.map(post, PostResponseDTO.class);
    }

    /** Convert PostRequestDTO to Post entity */
    public Post toPostEntity(PostRequestDTO postRequestDTO, User user, Topic topic) {
        Post post = modelMapper.map(postRequestDTO, Post.class);
        post.setUser(user);
        post.setTopic(topic);
        return post;
    }

    /** Converts a list of post entities to a list of PostResponseDTOs */
    public List<PostResponseDTO> toPostResponseDTOList(List<Post> posts) {
        return posts.stream().map(this::toPostResponseDTO).collect(Collectors.toList());
    }
}
