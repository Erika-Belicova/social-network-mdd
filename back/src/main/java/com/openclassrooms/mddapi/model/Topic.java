package com.openclassrooms.mddapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a topic.
 * Contains title and description, linked posts, and subscribed users.
 * Automatically tracks creation and update timestamps.
 */
@Entity
@Table(name = "TOPICS")
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // posts belonging to this topic

    @ManyToMany(mappedBy = "topics")
    private List<User> users = new ArrayList<>(); // users subscribed to this topic

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
