package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.post.PostRequestDTO;
import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import com.openclassrooms.mddapi.exception.PostNotFoundException;
import com.openclassrooms.mddapi.exception.TopicNotFoundException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository,
                       TopicRepository topicRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.postMapper = postMapper;
    }

    @Transactional
    public void savePost(Long userId, PostRequestDTO postRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        Topic topic = topicRepository.findById(postRequestDTO.getTopicId())
                .orElseThrow(() -> new TopicNotFoundException("Topic not found!"));

        Post post = postMapper.toPostEntity(postRequestDTO);
        post.setUser(user);
        post.setTopic(topic);
        postRepository.save(post);
    }

    @Transactional (readOnly = true)
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found!"));
        return postMapper.toPostResponseDTO(post);
    }

    @Transactional (readOnly = true)
    public List<PostResponseDTO> getAllPostsForTopic(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found!"));
        List<Post> posts = postRepository.findByTopicId(topicId);
        return postMapper.toPostResponseDTOList(posts);
    }

}