package com.app.features.admin.service;

import com.app.features.admin.dto.CommentDto;
import com.app.features.admin.model.Comment;
import com.app.features.admin.model.Post;
import com.app.features.admin.repository.CommentRepository;
import com.app.features.admin.repository.PostRepository;
import com.app.features.auth.model.User;
import com.app.features.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Post likePost(Long postId, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (post.getDislikes().contains(author)) {
            post.getDislikes().remove(author);
            post.getLikes().add(author);
        } else if (post.getLikes().contains(author)) {
            post.getLikes().remove(author);
        } else {
            post.getLikes().add(author);
        }

        return postRepository.save(post);
    }

    public Post dislikePost(Long postId, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (post.getLikes().contains(author)) {
            post.getLikes().remove(author);
            post.getDislikes().add(author);
        } else if (post.getDislikes().contains(author)) {
            post.getDislikes().remove(author);
        } else {
            post.getDislikes().add(author);
        }

        return postRepository.save(post);
    }

    public Comment addComment(Long postId, CommentDto commentDto, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found."));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Comment comment = Comment.builder()
                .post(post)
                .content(commentDto.getContent())
                .user(user)
                .build();
        return commentRepository.save(comment);
    }
}
