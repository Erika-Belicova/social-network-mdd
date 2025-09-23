package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Post entity with a method to find posts by topic ID.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTopicId(Long topicId);
    boolean existsByTitleAndTopicId(String title, Long topicId);
}
