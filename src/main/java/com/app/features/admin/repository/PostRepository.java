package com.app.features.admin.repository;

import com.app.features.admin.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByPublishDateDesc();

    //@Query("SELECT p FROM Post p WHERE p.author.id = :authorId")
    List<Post> findByAuthor_IdOrderByPublishDateDesc(Long authorId);
}
