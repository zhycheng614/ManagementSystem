package com.backend.management.repository;

import com.backend.management.model.Post;
import com.backend.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTenantId(User tenantId);
    Post findByIdAndTenantId(Long id, User tenantId);
}
