package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity, providing CRUD operations
 * and custom queries to find users by username or email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
