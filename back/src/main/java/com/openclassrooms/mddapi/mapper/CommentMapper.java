package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDTO;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDTO;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper component for converting between Comment entities and their DTO representations.
 * Handles mapping of nested user and post details for response DTOs.
 */
@Component
public class CommentMapper {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public CommentMapper(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    /** Convert Comment entity to CommentResponseDTO */
    public CommentResponseDTO toCommentResponseDTO(Comment comment) {
        CommentResponseDTO commentResponseDTO = modelMapper.map(comment, CommentResponseDTO.class);

        // manually map nested user details if present
        if (comment.getUser() != null) {
            commentResponseDTO.setUserId(comment.getUser().getId());
            commentResponseDTO.setUsername(comment.getUser().getUsername());
        }

        // manually map nested post id if present
        if (comment.getPost() != null) {
            commentResponseDTO.setPostId(comment.getPost().getId());
        }

        return commentResponseDTO;
    }

    /** Convert CommentRequestDTO to Comment entity */
    public Comment toCommentEntity(CommentRequestDTO commentRequestDTO) {
        return modelMapper.map(commentRequestDTO, Comment.class);
    }

    /** Converts a list of comment entities to a list of CommentResponseDTOs */
    public List<CommentResponseDTO> toCommentResponseDTOList(List<Comment> comments) {
        return comments.stream().map(this::toCommentResponseDTO).collect(Collectors.toList());
    }

}