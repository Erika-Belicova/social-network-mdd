package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.post.PostRequestDTO;
import com.openclassrooms.mddapi.dto.post.PostResponseDTO;
import com.openclassrooms.mddapi.exception.DuplicateFieldValidationException;
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
import java.util.stream.Collectors;

/**
 * Service layer for managing posts.
 * <p>
 * Provides operations to save posts, retrieve posts by ID, and fetch posts from
 * the topics a user is subscribed to. Integrates with repositories for posts,
 * users, and topics, and uses a mapper to convert between entities and DTOs.
 * </p>
 */
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
    public PostResponseDTO savePost(Long userId, PostRequestDTO postRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        Topic topic = topicRepository.findById(postRequestDTO.getTopicId())
                .orElseThrow(() -> new TopicNotFoundException("Topic not found!"));

        // check for duplicate title in the same topic
        if (postRepository.existsByTitleAndTopicId(postRequestDTO.getTitle(), topic.getId())) {
            throw new DuplicateFieldValidationException("A post with this title already exists!");
        }

        Post post = postMapper.toPostEntity(postRequestDTO);
        post.setUser(user);
        post.setTopic(topic);
        Post savedPost = postRepository.save(post);

        return postMapper.toPostResponseDTO(savedPost);
    }

    @Transactional (readOnly = true)
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found!"));
        return postMapper.toPostResponseDTO(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsForUserTopics(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        // get the topics the user is subscribed to
        List<Topic> topics = user.getTopics();

        // for each topic, get all posts and collect all posts into one list
        List<Post> allPosts = topics.stream()
                .flatMap(topic -> postRepository.findByTopicId(topic.getId()).stream())
                .collect(Collectors.toList());

        // map posts to PostResponseDTO
        return postMapper.toPostResponseDTOList(allPosts);
    }

}