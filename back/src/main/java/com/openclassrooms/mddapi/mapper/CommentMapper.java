package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDTO;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public CommentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /** Convert Comment entity to CommentResponseDTO */
    public CommentResponseDTO toCommentResponseDTO(Comment comment) {
        return modelMapper.map(comment, CommentResponseDTO.class);
    }

    /** Convert CommentRequestDTO to Comment entity */
    public Comment toCommentEntity(CommentRequestDTO commentRequestDTO, User user, Post post) {
        Comment comment = modelMapper.map(commentRequestDTO, Comment.class);
        comment.setUser(user);
        comment.setPost(post);
        return comment;
    }

    /** Converts a list of comment entities to a list of CommentResponseDTOs */
    public List<CommentResponseDTO> toCommentResponseDTOList(List<Comment> comments) {
        return comments.stream().map(this::toCommentResponseDTO).collect(Collectors.toList());
    }

}